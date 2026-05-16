package com.virtualworld.agendadeventas.ui.screen.purchases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.core.entity.inventoryForStore
import com.virtualworld.agendadeventas.domain.usecase.GetProductAllUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoresActiveUseCase
import com.virtualworld.agendadeventas.domain.usecase.UpdateInventoryUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PurchaseProductRow(
    val id: Long,
    val nombre: String,
    val compra: Float,
    val stockAtStore: Int,
)

@HiltViewModel
class PurchasesViewModel @Inject constructor(
    private val getProductAllUseCase: GetProductAllUseCase,
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val updateInventoryUseCase: UpdateInventoryUseCase,
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

    init {
        observeStores()
        observeProducts()
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
        refreshRowsAndAmounts(resetAmounts = true)
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

    fun confirmPurchases() {
        viewModelScope.launch {
            val storeId = _selectedStoreId.value ?: return@launch
            _screenUiState.update { ScreenUiState.LOADING }
            val amounts = _addAmounts.value
            amounts.forEach { (productId, amount) ->
                if (amount > 0) {
                    updateInventoryUseCase.addStock(productId, storeId, amount)
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
