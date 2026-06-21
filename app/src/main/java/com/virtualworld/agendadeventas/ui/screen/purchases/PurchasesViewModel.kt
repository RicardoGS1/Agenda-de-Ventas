package com.virtualworld.agendadeventas.ui.screen.purchases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.core.entity.inventoryForStore
import com.virtualworld.agendadeventas.core.model.StoreCashSummaryCore
import com.virtualworld.agendadeventas.core.repository.InsufficientCashException
import com.virtualworld.agendadeventas.domain.usecase.GetProductAllUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoreCashSummariesUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoresActiveUseCase
import com.virtualworld.agendadeventas.domain.usecase.RecordPurchaseUseCase
import com.virtualworld.agendadeventas.domain.usecase.UpdateInventoryUseCase
import com.virtualworld.agendadeventas.domain.usecase.WithdrawCashFromStoreUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PurchaseProductRow(
    val id: Long,
    val nombre: String,
    val compra: Float,
    val stockAtStore: Int,
    val imagePath: String? = null,
)

@HiltViewModel
class PurchasesViewModel @Inject constructor(
    private val getProductAllUseCase: GetProductAllUseCase,
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val updateInventoryUseCase: UpdateInventoryUseCase,
    private val recordPurchaseUseCase: RecordPurchaseUseCase,
    private val getStoreCashSummariesUseCase: GetStoreCashSummariesUseCase,
    private val withdrawCashFromStoreUseCase: WithdrawCashFromStoreUseCase,
) : ViewModel() {

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    private val _storesActiveState = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val storesActiveState: StateFlow<List<Pair<Int, String>>> = _storesActiveState

    private val _allProducts = MutableStateFlow<List<ProductRoom>>(emptyList())

    private val _selectedStoreId = MutableStateFlow<Int?>(null)
    val selectedStoreId: StateFlow<Int?> = _selectedStoreId

    private val _purchaseRows = MutableStateFlow<List<PurchaseProductRow>>(emptyList())
    val purchaseRows: StateFlow<List<PurchaseProductRow>> = _purchaseRows

    private val _addAmounts = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val addAmounts: StateFlow<Map<Long, Int>> = _addAmounts

    private val _storeSummaries = MutableStateFlow<List<StoreCashSummaryCore>>(emptyList())

    private val _storeCashBalance = MutableStateFlow(0f)
    val storeCashBalance: StateFlow<Float> = _storeCashBalance

    private val _deductFromCash = MutableStateFlow(true)
    val deductFromCash: StateFlow<Boolean> = _deductFromCash

    private val _insufficientCashError = MutableStateFlow(false)
    val insufficientCashError: StateFlow<Boolean> = _insufficientCashError

    val purchaseTotal: StateFlow<Float> = combine(_purchaseRows, _addAmounts) { rows, amounts ->
        rows.sumOf { row ->
            val qty = amounts[row.id] ?: 0
            (row.compra * qty).toDouble()
        }.toFloat()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0f)

    val remainingBalance: StateFlow<Float> = combine(
        purchaseTotal,
        _storeCashBalance,
        _deductFromCash,
    ) { total, balance, deduct ->
        if (deduct) balance - total else balance
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0f)

    init {
        observeStores()
        observeProducts()
        observeCashSummaries()
    }

    private fun observeStores() {
        getStoresActiveUseCase.getStoresActive().onEach { state ->
            when (state) {
                is NetworkResponseState.Error -> _screenUiState.update { ScreenUiState.ERROR }
                is NetworkResponseState.Loading -> Unit
                is NetworkResponseState.Success -> {
                    _storesActiveState.update {
                        state.result.map { Pair(it.idStore, it.nameStore) }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeCashSummaries() {
        getStoreCashSummariesUseCase().onEach { state ->
            when (state) {
                is NetworkResponseState.Error -> Unit
                is NetworkResponseState.Loading -> Unit
                is NetworkResponseState.Success -> {
                    _storeSummaries.update { state.result }
                    updateStoreBalance()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateStoreBalance() {
        val storeId = _selectedStoreId.value
        val balance = if (storeId == null) {
            0f
        } else {
            _storeSummaries.value.find { it.storeId == storeId }?.balance ?: 0f
        }
        _storeCashBalance.update { balance }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            getProductAllUseCase().collect { state ->
                when (state) {
                    is NetworkResponseState.Loading -> Unit
                    is NetworkResponseState.Error -> _screenUiState.update { ScreenUiState.ERROR }
                    is NetworkResponseState.Success -> {
                        _allProducts.update { state.result }
                        refreshRowsAndAmounts(resetAmounts = false)
                        _screenUiState.update { ScreenUiState.NEUTRAL }
                    }
                }
            }
        }
    }

    fun selectStore(storeId: Int?) {
        _selectedStoreId.value = storeId
        updateStoreBalance()
        refreshRowsAndAmounts(resetAmounts = true)
    }

    fun setDeductFromCash(enabled: Boolean) {
        _deductFromCash.update { enabled }
        _insufficientCashError.update { false }
    }

    private fun refreshRowsAndAmounts(resetAmounts: Boolean) {
        val storeId = _selectedStoreId.value
        if (storeId == null || storeId !in 1..5) {
            _purchaseRows.update { emptyList() }
            if (resetAmounts) _addAmounts.update { emptyMap() }
            return
        }
        val rows = _allProducts.value
            .filter { it.tieneInventario }
            .map { product ->
                PurchaseProductRow(
                    id = product.id,
                    nombre = product.nombre,
                    compra = product.compra,
                    stockAtStore = product.inventoryForStore(storeId),
                    imagePath = product.imagePath,
                )
            }
        _purchaseRows.update { rows }
        if (resetAmounts) {
            _addAmounts.update { rows.associate { it.id to 0 } }
        } else {
            _addAmounts.update { current ->
                rows.associate { row -> row.id to (current[row.id] ?: 0) }
            }
        }
    }

    fun incrementAmount(productId: Long) {
        val row = _purchaseRows.value.find { it.id == productId } ?: return
        if (_deductFromCash.value) {
            val newTotal = purchaseTotal.value + row.compra
            if (newTotal > _storeCashBalance.value) return
        }
        _insufficientCashError.update { false }
        _addAmounts.update { current ->
            current + (productId to (current[productId] ?: 0) + 1)
        }
    }

    fun decrementAmount(productId: Long) {
        _addAmounts.update { current ->
            val prev = current[productId] ?: 0
            if (prev > 0) current + (productId to prev - 1)
            else current
        }
    }

    fun confirmPurchases(withdrawNote: String) {
        viewModelScope.launch {
            val storeId = _selectedStoreId.value ?: return@launch
            val total = purchaseTotal.value
            if (total <= 0f) return@launch

            _screenUiState.update { ScreenUiState.LOADING }

            if (_deductFromCash.value) {
                when (val result = withdrawCashFromStoreUseCase(storeId, total, withdrawNote)) {
                    is NetworkResponseState.Success -> Unit
                    is NetworkResponseState.Error -> {
                        if (result.exception is InsufficientCashException) {
                            _insufficientCashError.update { true }
                            _screenUiState.update { ScreenUiState.NEUTRAL }
                        } else {
                            _screenUiState.update { ScreenUiState.ERROR }
                        }
                        return@launch
                    }
                    is NetworkResponseState.Loading -> return@launch
                }
            }

            val amounts = _addAmounts.value
            val deductedFromCash = _deductFromCash.value
            amounts.forEach { (productId, amount) ->
                if (amount > 0) {
                    val row = _purchaseRows.value.find { it.id == productId } ?: return@forEach
                    updateInventoryUseCase.addStock(productId, storeId, amount)
                    recordPurchaseUseCase(
                        productId = productId,
                        productName = row.nombre,
                        unitCost = row.compra,
                        storeId = storeId,
                        units = amount,
                        deductedFromCash = deductedFromCash,
                    )
                }
            }
            _addAmounts.update { _purchaseRows.value.associate { it.id to 0 } }
            _screenUiState.update { ScreenUiState.OK }
        }
    }

    fun changerUiState(state: ScreenUiState) {
        _screenUiState.update { state }
    }
}
