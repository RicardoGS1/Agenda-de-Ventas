package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.domain.mapper.toProductRoom
import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive
import com.virtualworld.agendadeventas.domain.models.StoresValues
import com.virtualworld.agendadeventas.domain.usecase.AddProductUseCase
import com.virtualworld.agendadeventas.domain.usecase.DeleteProductUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetProductForStore
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.common.checkDecimalNumber
import com.virtualworld.agendadeventas.ui.screen.common.productValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val getProductForStore: GetProductForStore,
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {


    private val _productsState =
        MutableStateFlow(listOf(ProductWithStoresActive()))
    val productsState: StateFlow<List<ProductWithStoresActive>> = _productsState

    private val _productSelectState =
        MutableStateFlow(ProductWithStoresActive())
    val productSelectState: StateFlow<ProductWithStoresActive> = _productSelectState

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    init {
        getProductForStoresActive()
    }

    private fun getProductForStoresActive() {
        viewModelScope.launch {
            getProductForStore.getProductsWithActivesStores().collect { state ->

                when (state) {
                    is NetworkResponseState.Error -> _screenUiState.update { ScreenUiState.ERROR }
                    is NetworkResponseState.Loading -> _screenUiState.update { ScreenUiState.LOADING }
                    is NetworkResponseState.Success -> {
                        _productsState.update {
                            state.result
                        }

                        if (_screenUiState.value != ScreenUiState.OK)
                            _screenUiState.update { ScreenUiState.NEUTRAL }
                    }
                }
            }
        }
    }

    fun changerUiState(message: ScreenUiState) {
        _screenUiState.update {
            message
        }
    }

    fun changeProductSelect(product: ProductWithStoresActive) {
        _productSelectState.update {
            product
        }

    }

    fun updateStoreValue(storeId: String, value: String) {
        _productSelectState.update { currentState ->
            currentState.copy(
                storesValues = currentState.storesValues.map {
                    if (it.idStore == storeId)
                        StoresValues(storeId, it.nameStore, checkDecimalNumber(value))
                    else
                        it
                }
            )
        }
    }

    fun updateProductName(name: String) {
        _productSelectState.update { it.copy(productName = name) }
    }

    fun updateProductCost(cost: String) {
        _productSelectState.update { it.copy(productCost = checkDecimalNumber(cost)) }
    }

    fun setProduct() {

        viewModelScope.launch {
            //changerUiState(ScreenUiState.LOADING)

            if (productValidator(_productSelectState.value)) {

                val product = productSelectState.value
                val productRoom = product.toProductRoom()

                val result = addProductUseCase.updateProduct(productRoom)

                val state = when (result) {
                    is NetworkResponseState.Error -> ScreenUiState.ERROR
                    is NetworkResponseState.Loading -> ScreenUiState.LOADING
                    is NetworkResponseState.Success -> ScreenUiState.OK
                }
                changerUiState(state)
            } else {
                changerUiState(ScreenUiState.ERROR)

            }
        }
    }

    fun deleteProduct(product: ProductWithStoresActive) {
        viewModelScope.launch {
            deleteProductUseCase(product.idProduct.toInt())
        }
    }


}