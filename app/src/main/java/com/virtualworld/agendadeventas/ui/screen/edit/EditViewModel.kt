package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualword3d.salesregister.Data.Entity.ProductRoom

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.domain.Model.ProductWithStoresActive
import com.virtualworld.agendadeventas.domain.Model.StoresValues
import com.virtualworld.agendadeventas.domain.usecase.AddProductUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetProductForStore
import com.virtualworld.agendadeventas.ui.screen.add.checkDecimalNumber
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val getProductForStore: GetProductForStore,
    private val addProductUseCase: AddProductUseCase
) : ViewModel() {


    private val _productsState =
        MutableStateFlow<List<ProductWithStoresActive>>(listOf(ProductWithStoresActive()))
    val productsState: StateFlow<List<ProductWithStoresActive>> = _productsState

    private val _productSelectState =
        MutableStateFlow<ProductWithStoresActive>(ProductWithStoresActive())
    val productSelectState: StateFlow<ProductWithStoresActive> = _productSelectState

    private val _screenUiState = MutableStateFlow<ScreenUiState>(ScreenUiState.LOADING)
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
        println(message)
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

                //currentState.storesValues + ("storeId" to checkDecimalNumber(value))
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

            if (_productSelectState.value.productName != "" && _productSelectState.value.productCost != "") {

                val listMap = _productSelectState.value.storesValues.associate {
                    it.idStore.toInt() to it.value
                }

                val productRoom = ProductRoom(
                    id = _productSelectState.value.idProduct.toLong(),
                    nombre = _productSelectState.value.productName,
                    compra = _productSelectState.value.productCost.toLong(),
                    venta1 = listMap[1]?.toLong() ?: 0,
                    venta2 = listMap[2]?.toLong() ?: 0,
                    venta3 = listMap[3]?.toLong() ?: 0,
                    venta4 = listMap[4]?.toLong() ?: 0,
                    venta5 = listMap[5]?.toLong() ?: 0
                )


                val result = addProductUseCase.updateProduct(productRoom)

                val state = when (result) {
                    is NetworkResponseState.Error -> ScreenUiState.ERROR
                    is NetworkResponseState.Loading -> ScreenUiState.LOADING
                    is NetworkResponseState.Success -> ScreenUiState.OK
                }

                changerUiState(state)
                // restartProductUI()

            } else {
                changerUiState(ScreenUiState.ERROR)

            }
        }
    }


}