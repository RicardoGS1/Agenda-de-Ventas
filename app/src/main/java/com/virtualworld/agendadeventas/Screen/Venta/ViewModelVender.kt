package com.virtualworld.agendadeventas.Screen.Venta

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualword3d.salesregister.Data.Entity.Producto
import com.virtualword3d.salesregister.Data.Entity.Tiendas
import com.virtualword3d.salesregister.Data.Entity.Vendido
import com.virtualword3d.salesregister.Repocitory.ProductoLocalDataSource
import com.virtualword3d.salesregister.Repocitory.TiendasLocalDataSource
import com.virtualword3d.salesregister.Repocitory.VendidoLocalDataSourse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class ViewModelVender @Inject constructor(private val productoRepo: ProductoLocalDataSource, private val tiendasRepo: TiendasLocalDataSource, private val vendidoRepo: VendidoLocalDataSourse) : ViewModel() {

    private var numTienda = 0
    private var productos:List<Producto>? = null
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


    private fun obtenerPrimeraTiends(tiendas: List<Tiendas>) {

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
                                LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy")).toString()
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


