package com.virtualworld.agendadeventas.ui.screen.purchases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.domain.usecase.GetProductAllUseCase
import com.virtualworld.agendadeventas.domain.usecase.UpdateInventoryUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchasesViewModel @Inject constructor(
    private val getProductAllUseCase: GetProductAllUseCase,
    private val updateInventoryUseCase: UpdateInventoryUseCase,
) : ViewModel() {

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    private val _productsWithInventory = MutableStateFlow<List<ProductRoom>>(emptyList())
    val productsWithInventory: StateFlow<List<ProductRoom>> = _productsWithInventory

    private val _addAmounts = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val addAmounts: StateFlow<Map<Long, Int>> = _addAmounts

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            getProductAllUseCase().collect { state ->
                when (state) {
                    is NetworkResponseState.Loading -> _screenUiState.update { ScreenUiState.LOADING }
                    is NetworkResponseState.Error -> _screenUiState.update { ScreenUiState.ERROR }
                    is NetworkResponseState.Success -> {
                        val withInventory = state.result.filter { it.tieneInventario }
                        _productsWithInventory.update { withInventory }
                        _addAmounts.update { withInventory.associate { it.id to 0 } }
                        _screenUiState.update { ScreenUiState.NEUTRAL }
                    }
                }
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
            val current_ = current[productId] ?: 0
            if (current_ > 0) current + (productId to current_ - 1)
            else current
        }
    }

    fun confirmPurchases() {
        viewModelScope.launch {
            _screenUiState.update { ScreenUiState.LOADING }
            val amounts = _addAmounts.value
            amounts.forEach { (productId, amount) ->
                if (amount > 0) {
                    updateInventoryUseCase.addStock(productId, amount)
                }
            }
            _screenUiState.update { ScreenUiState.OK }
        }
    }

    fun changerUiState(state: ScreenUiState) {
        _screenUiState.update { state }
    }
}
