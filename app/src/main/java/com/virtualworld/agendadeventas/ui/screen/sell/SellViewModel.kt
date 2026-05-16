package com.virtualworld.agendadeventas.ui.screen.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState

import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.ProductStoreCore
import com.virtualworld.agendadeventas.core.source.local.SoldLocalDataSource
import com.virtualworld.agendadeventas.domain.usecase.GetProductForStore
import com.virtualworld.agendadeventas.domain.usecase.GetStoresActiveUseCase
import com.virtualworld.agendadeventas.domain.usecase.UpdateInventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class SellViewModel @Inject constructor(
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val getProductStore: GetProductForStore,
    private val vendidoRepo: SoldLocalDataSource,
    private val updateInventoryUseCase: UpdateInventoryUseCase,
) : ViewModel() {


    private val _storesActiveState = MutableStateFlow(listOf(Pair(-1, "")))
    val storesActiveState: StateFlow<List<Pair<Int, String>>> = _storesActiveState

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    private val _productForStore = MutableStateFlow(listOf(ProductStoreCore(0, "", 0f, 0f)))
    val productForStore: StateFlow<List<ProductStoreCore>> = _productForStore

    private val _listChangerProductsSell = MutableStateFlow(mutableListOf(Pair(0, 0)))
    val listChangerProductsSell: StateFlow<List<Pair<Int, Int>>> = _listChangerProductsSell



    init {

        getStoresActive()
    }

    fun changerUiState(message: ScreenUiState){
        _screenUiState.update {
            message
        }
    }


    private fun getStoresActive() {

        getStoresActiveUseCase.getStoresActive().onEach { state ->

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
        }.launchIn(viewModelScope)

    }



    fun getProductForStore(idStore: Int) {


        getProductStore.getProductForStore(idStore).onEach { state ->

            when (state) {
                is NetworkResponseState.Error -> println("")
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> {
                    _productForStore.update { state.result }
                    startUnitProduct()
                }
            }
        }.launchIn(viewModelScope)


    }

    private fun startUnitProduct() {



       val a = _productForStore.value.map { product ->
            Pair(product.idProduct, 0)
        }.toMutableList()

       _listChangerProductsSell.update {

           a
        }

        println(_listChangerProductsSell.value)

    }

    fun changerUnitSell(changer: Pair<Int, Int>) {
        val product = _productForStore.value.find { it.idProduct == changer.first }

        val a = _listChangerProductsSell.value.map { pair ->
            if (pair.first == changer.first) {
                val newQty = pair.second + changer.second
                val clampedQty = if (product?.tieneInventario == true) {
                    newQty.coerceIn(0, product.inventario)
                } else {
                    maxOf(0, newQty)
                }
                Pair(pair.first, clampedQty)
            } else {
                pair
            }
        }.toMutableList()

        _listChangerProductsSell.update { a }
    }

    fun salveSell(selectedIndex: Int) {

        viewModelScope.launch {

        val selectedCalendar = Instant.now().toEpochMilli()


        val b = mutableListOf<SoldRoom>()

         _productForStore.value.map { product ->

            _listChangerProductsSell.value.find { pair -> pair.first == product.idProduct && pair.second != 0 }
                ?.let {

                    b.add(
                        SoldRoom(
                            idprod = product.idProduct.toLong(),
                            nombre = product.nombre,
                            compra = product.compra,
                            valor = product.venta,
                            tienda = selectedIndex,
                            unidades = it.second,
                            fecha = selectedCalendar
                        )
                    )
                }


        }

        vendidoRepo.addProductsSold(b)

        b.forEach { sold ->
            val product = _productForStore.value.find { it.idProduct == sold.idprod.toInt() }
            if (product?.tieneInventario == true) {
                updateInventoryUseCase.discountStock(sold.idprod, selectedIndex, sold.unidades)
            }
        }

        changerUiState(ScreenUiState.OK)
        startUnitProduct()
        }
    }


}


