package com.virtualworld.agendadeventas.ui.Screen.Venta

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualword3d.salesregister.Data.Entity.Vendido
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.source.local.ProductoLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
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
class ViewModelVender @Inject constructor( private val getStoresActiveUseCase: GetStoresActiveUseCase, private val getProductStore: GetProductStore, private val productoRepo: ProductoLocalDataSource, private val tiendasRepo: TiendasLocalDataSource, private val vendidoRepo: VendidoLocalDataSourse) : ViewModel() {


    private val _storesActiveState = MutableStateFlow(listOf( Pair(-1,"")))
    val storesActiveState : StateFlow<List<Pair<Int,String>>> = _storesActiveState

    private val _messengerState = MutableStateFlow("")
    val messengerState : StateFlow<String> = _messengerState

    private val _productForStore = MutableStateFlow(listOf (ProductStoreCore(0,"",0,0,)))
    val productForStore : StateFlow<List<ProductStoreCore>> = _productForStore



    fun getStoresActive(){

        getStoresActiveUseCase.GetTiendasActivas().onEach { state->

            when(state){
                is NetworkResponseState.Error -> _messengerState.update { state.exception.toString() }
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> {_storesActiveState.update {



                    state.result.map {

                        Pair( it.idStore, it.nameStore )

                    }



                }  }
            }
        }.launchIn(viewModelScope)

   }

    fun getProductForStore(idStore:Int){


        getProductStore.getProductStore(idStore).onEach {state->

            when(state){
                is NetworkResponseState.Error ->  println("")
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> {_productForStore.update { state.result }  }
            }
        }.launchIn(viewModelScope)



    }







    private var numTienda = 0
    private var productos:List<ProductRoom>? = null
    private var vender: MutableList<Vendido>? = null

    private val _respuestaError= MutableLiveData(Mensajes.NEUTRO)
    val respuestaError : LiveData<Mensajes> = _respuestaError

    private val _listaUnidades = MutableLiveData<MutableList<Int>>()
    val listaUnidades : LiveData<MutableList<Int>> = _listaUnidades

    private val _actualisarcompouse = MutableLiveData<Int>(0)
    val actualisarcompouse: LiveData<Int> = _actualisarcompouse

    private val _nombreProducto = MutableLiveData<List<String>>()
    val nombreProducto: LiveData<List<String>> = _nombreProducto

    private val _tiendaSeleccionada = MutableLiveData<String>()
    val tiendaSeleccionada: LiveData<String> = _tiendaSeleccionada

    private val _nombreTienda1 = MutableLiveData<String>()
    val nombreTienda1: LiveData<String> = _nombreTienda1

    private val _nombreTienda2 = MutableLiveData<String>()
    val nombreTienda2: LiveData<String> = _nombreTienda2

    private val _nombreTienda3 = MutableLiveData<String>()
    val nombreTienda3: LiveData<String> = _nombreTienda3

    private val _nombreTienda4 = MutableLiveData<String>()
    val nombreTienda4: LiveData<String> = _nombreTienda4

    private val _nombreTienda5 = MutableLiveData<String>()
    val nombreTienda5: LiveData<String> = _nombreTienda5


    private fun obtenerPrimeraTiends(tiendas: List<StoreRoom>) {

        for (i in 0..4) {
            if (tiendas[i].activa) {
                changeTienda(tiendas[i].nombre)
                break
            }
        }
    }

    private fun inisializarUnidadesVenta() {
        val temp: MutableList<Int> = mutableListOf()

        for (i in 0..productos!!.size - 1) {
            temp.add(i, 0)
        }
        _listaUnidades.value=temp
    }

    private fun maxId(callback: (Long) -> Unit) {
        return vendidoRepo.maxId(callback)
    }


    fun getProducto() {
        productoRepo.getAllLogs {

             productos=it
            _nombreProducto.value = it.map {
                it.nombre
            }
            inisializarUnidadesVenta()
        }
    }

    fun getTiendas() {

        tiendasRepo.getTiendas {

            if (it[0].activa)
                _nombreTienda1.value = it[0].nombre

            if (it[1].activa)
                _nombreTienda2.value = it[1].nombre


            if (it[2].activa)
                _nombreTienda3.value = it[2].nombre


            if (it[3].activa)
                _nombreTienda4.value = it[3].nombre


            if (it[4].activa)
                _nombreTienda5.value = it[4].nombre


            obtenerPrimeraTiends(it)

        }
    }

    fun changeTienda(selectionOption: String) {
        _tiendaSeleccionada.value = selectionOption

        when (_tiendaSeleccionada.value) {
            _nombreTienda1.value -> {
                numTienda = 0
            }

            _nombreTienda2.value -> {
                numTienda = 1
            }

            _nombreTienda3.value -> {
                numTienda = 2
            }

            _nombreTienda4.value -> {
                numTienda = 3
            }

            _nombreTienda5.value -> {
                numTienda = 4
            }
        }
    }

    fun addUnidades(index:Int){
       _listaUnidades.value!![index]++
    }

    fun resUnidades(index:Int){
        _listaUnidades.value!![index]--
    }

    fun actualisarya(){
        _actualisarcompouse.value = _actualisarcompouse.value!! + 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun GuardarVenta() {

        val selectedCalendar = Instant.now().toEpochMilli()




        //OBTIENE EL MAXIMO ID DE LA MASE DATOS Vendidos
        maxId {

            val maxidbd = it + 1

                //VARIABLE PARA GUARDAR LA NUEVA LISTA QUE SE VA ENVIAR A GUARDAR EN LA BASE DATTOS DE DE VENDIDOS
                vender = mutableListOf()
                var contVentasAceptadas = 0

                //RECORRE LA LISTA PRODUCTOS
                for (i in 0..productos!!.size - 1) {

                    //GUARDAR EN valor EL VALOR DE VENTA DE LA TIENDA QUE SE ESTA VENDIENDO
                    var valor: Long = 0

                    when (_tiendaSeleccionada.value) {
                        _nombreTienda1.value -> {
                            valor = productos!![i].venta1

                        }

                        _nombreTienda2.value -> {
                            valor = productos!![i].venta2

                        }

                        _nombreTienda3.value -> {
                            valor = productos!![i].venta3

                        }

                        _nombreTienda4.value -> {
                            valor = productos!![i].venta4

                        }

                        _nombreTienda5.value -> {
                            valor = productos!![i].venta5

                        }
                    }


                    //CONFORMA LA NUEVA LISTA

                    if (_listaUnidades.value!![i] != 0) {

                        vender!!.add(
                            Vendido(
                                maxidbd + contVentasAceptadas,
                                productos!![i].id,
                                productos!![i].nombre,
                                productos!![i].compra,
                                valor,
                                numTienda,
                                _listaUnidades.value!![i],
                                selectedCalendar
                            )
                        )
                        contVentasAceptadas++

                    }
                }

                Log.d(
                    "ViewModelVender ",
                    "Lista de productos enviados a vender: " + vender.toString()
                )
                vendidoRepo.addProductoVendido(vender!!)

            inisializarUnidadesVenta()
            actualisarya()
            controlMensaje(Mensajes.BIEN)
        }

    }

    fun controlMensaje(mensaje: Mensajes){
        _respuestaError.value=mensaje
    }

}


