package com.virtualworld.agendadeventas.ui.Screen.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualword3d.salesregister.Data.Entity.Mensajes

import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.source.local.VendidoLocalDataSourse
import com.virtualworld.agendadeventas.domain.UseCase.GetProductStore
import com.virtualworld.agendadeventas.domain.UseCase.GetStoresActiveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class SellViewModel @Inject constructor(
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val getProductStore: GetProductStore,
    private val vendidoRepo: VendidoLocalDataSourse
) : ViewModel() {


    private val _storesActiveState = MutableStateFlow(listOf(Pair(-1, "")))
    val storesActiveState: StateFlow<List<Pair<Int, String>>> = _storesActiveState

    private val _messengerState = MutableStateFlow(Mensajes.CARGANDO)
    val messengerState: StateFlow<Mensajes> = _messengerState

    private val _productForStore = MutableStateFlow(listOf(ProductStoreCore(0, "", 0, 0)))
    val productForStore: StateFlow<List<ProductStoreCore>> = _productForStore

    private val _listChangerProductsSell = MutableStateFlow(mutableListOf(Pair(0, 0)))
    val listChangerProductsSell: StateFlow<List<Pair<Int, Int>>> = _listChangerProductsSell



    init {

        getStoresActive()
    }

    fun changerMessenger(message: Mensajes){
        _messengerState.update {
            message
        }
    }


    fun getStoresActive() {

        getStoresActiveUseCase.GetTiendasActivas().onEach { state ->

            when (state) {
                is NetworkResponseState.Error -> _messengerState.update { Mensajes.ERROR }
                is NetworkResponseState.Loading -> Mensajes.CARGANDO
                is NetworkResponseState.Success -> {

                    _storesActiveState.update {
                        state.result.map { Pair(it.idStore, it.nameStore) }
                    }

                    _messengerState.update {
                        Mensajes.NEUTRO
                    }
                }
            }
        }.launchIn(viewModelScope)

    }



    fun getProductForStore(idStore: Int) {


        getProductStore.getProductStore(idStore).onEach { state ->

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

        val a = _listChangerProductsSell.value.map { pair ->

            if (pair.first == changer.first) Pair(pair.first, pair.second + changer.second)
            else Pair(pair.first, pair.second)

        }.toMutableList()

        _listChangerProductsSell.update {
            a
        }


    }

    fun SalveSell(selectedIndex: Int) {

        val selectedCalendar = Instant.now().toEpochMilli()


        val b = mutableListOf<SoldRoom>()

        val a = _productForStore.value.map { product ->

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

        vendidoRepo.addProductoVendido(b)
        changerMessenger(Mensajes.BIEN)
        startUnitProduct()
    }


}


