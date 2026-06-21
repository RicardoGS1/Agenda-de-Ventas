package com.virtualworld.agendadeventas.ui.screen.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.core.entity.inventoryForStore
import com.virtualworld.agendadeventas.core.model.PurchaseForStoreCore
import com.virtualworld.agendadeventas.domain.usecase.GetProductAllUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetProductPurchaseHistoryUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoresActiveUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryProductRow(
    val id: Long,
    val nombre: String,
    val stockAtStore: Int,
    val imagePath: String? = null,
)

data class InventoryHistoryTarget(
    val productId: Long,
    val productName: String,
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getProductAllUseCase: GetProductAllUseCase,
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val getProductPurchaseHistoryUseCase: GetProductPurchaseHistoryUseCase,
) : ViewModel() {

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    private val _storesActiveState = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val storesActiveState: StateFlow<List<Pair<Int, String>>> = _storesActiveState

    private val _allProducts = MutableStateFlow<List<ProductRoom>>(emptyList())

    private val _selectedStoreId = MutableStateFlow<Int?>(null)

    private val _inventoryRows = MutableStateFlow<List<InventoryProductRow>>(emptyList())
    val inventoryRows: StateFlow<List<InventoryProductRow>> = _inventoryRows

    private val _historyTarget = MutableStateFlow<InventoryHistoryTarget?>(null)
    val historyTarget: StateFlow<InventoryHistoryTarget?> = _historyTarget

    private val _purchaseHistory = MutableStateFlow<List<PurchaseForStoreCore>>(emptyList())
    val purchaseHistory: StateFlow<List<PurchaseForStoreCore>> = _purchaseHistory

    private val _historyLoading = MutableStateFlow(false)
    val historyLoading: StateFlow<Boolean> = _historyLoading

    private var historyJob: Job? = null

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
                        refreshRows()
                        _screenUiState.update { ScreenUiState.NEUTRAL }
                    }
                }
            }
        }
    }

    fun selectStore(storeId: Int?) {
        _selectedStoreId.value = storeId
        refreshRows()
        closePurchaseHistory()
    }

    private fun refreshRows() {
        val storeId = _selectedStoreId.value
        if (storeId == null || storeId !in 1..5) {
            _inventoryRows.update { emptyList() }
            return
        }
        val rows = _allProducts.value
            .filter { it.tieneInventario }
            .map { product ->
                InventoryProductRow(
                    id = product.id,
                    nombre = product.nombre,
                    stockAtStore = product.inventoryForStore(storeId),
                    imagePath = product.imagePath,
                )
            }
        _inventoryRows.update { rows }
    }

    fun openPurchaseHistory(productId: Long, productName: String) {
        val storeId = _selectedStoreId.value ?: return
        _historyTarget.update { InventoryHistoryTarget(productId, productName) }
        _purchaseHistory.update { emptyList() }
        historyJob?.cancel()
        historyJob = viewModelScope.launch {
            _historyLoading.update { true }
            val state = getProductPurchaseHistoryUseCase(storeId, productId)
                .first { it !is NetworkResponseState.Loading }
            if (state is NetworkResponseState.Success) {
                _purchaseHistory.update { state.result }
            }
            _historyLoading.update { false }
        }
    }

    fun closePurchaseHistory() {
        historyJob?.cancel()
        _historyTarget.update { null }
        _purchaseHistory.update { emptyList() }
        _historyLoading.update { false }
    }

    fun changerUiState(state: ScreenUiState) {
        _screenUiState.update { state }
    }
}
