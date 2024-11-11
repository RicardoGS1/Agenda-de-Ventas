package com.virtualworld.agendadeventas.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.common.NetworkResponseState


import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.domain.usecase.AddProductUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoresActiveUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ProductUiState
import com.virtualworld.agendadeventas.ui.screen.common.checkDecimalNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddViewModel @Inject constructor(
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val addProductUseCase: AddProductUseCase,
) : ViewModel() {

    private val _storesActiveState = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val storesActiveState: StateFlow<List<Pair<Int, String>>> = _storesActiveState

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    private val _productUiState = MutableStateFlow(ProductUiState())
    val productUiState: StateFlow<ProductUiState> = _productUiState

    init {
        getStoresActive()
    }

    private fun getStoresActive() {
        viewModelScope.launch {
            getStoresActiveUseCase.getStoresActive().collect { state ->

                when (state) {
                    is NetworkResponseState.Error -> _screenUiState.update { ScreenUiState.ERROR }
                    is NetworkResponseState.Loading -> ScreenUiState.LOADING
                    is NetworkResponseState.Success -> {

                        _storesActiveState.update {
                            state.result.map { Pair(it.idStore, it.nameStore) }
                        }


                        _screenUiState.update {
                            ScreenUiState.NEUTRAL
                        }
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

    fun updateStoreValue(storeId: Int, value: String) {
        _productUiState.update { currentState ->
            currentState.copy(
                storeValues = currentState.storeValues + (storeId to checkDecimalNumber(value))
            )
        }
    }

    fun updateProductName(name: String) {
        _productUiState.update { it.copy(productName = name) }
    }

    fun updateProductCost(cost: String) {
        _productUiState.update { it.copy(productCost = checkDecimalNumber(cost)) }
    }

    private fun restartProductUI() {
        _productUiState.update { ProductUiState() }
    }


    fun setProduct() {

        viewModelScope.launch {
            changerUiState(ScreenUiState.LOADING)

            if (_productUiState.value.productName != "" && _productUiState.value.productCost != "") {

                val result = addProductUseCase(_productUiState.value)
                changerUiState(result)
                restartProductUI()

            } else {
                changerUiState(ScreenUiState.ERROR)

            }
        }
    }





}
