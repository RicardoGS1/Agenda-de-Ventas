package com.virtualword3d.salesregister.Screen.Registro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.VendidoLocalDataSourse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RecordViewModel @Inject constructor(private val vendidoRepo: VendidoLocalDataSourse, private val tiendasRepo: TiendasLocalDataSource) : ViewModel() {

    private var numTienda = 0

    private val _unidadesProducto = MutableLiveData<List<Int>>()
    val unidadesProducto: LiveData<List<Int>> = _unidadesProducto

    private val _nombreProducto = MutableLiveData<List<String>>()
    val nombreProducto: LiveData<List<String>> = _nombreProducto

    private val _costoProducto = MutableLiveData<List<Long>>()
    val costoProducto: LiveData<List<Long>> = _costoProducto

    private val _precioProducto = MutableLiveData<List<Long>>()
    val precioProducto: LiveData<List<Long>> = _precioProducto

    private val _fechaVenta = MutableLiveData<List<String>>()
    val fechaVenta: LiveData<List<String>> = _fechaVenta


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
        allVendidos()
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

    //OBTENER TODOS LOS PRODUCTOS VENDIDOS
    fun allVendidos() {
        vendidoRepo.getAllVendidos{

            _nombreProducto.value=it.filter {
                it.tienda==numTienda
            }.map {
                it.nombre
            }

            _unidadesProducto.value=it.filter {
                it.tienda==numTienda
            }.map {
                it.unidades
            }


            _costoProducto.value=it.filter {
                it.tienda==numTienda
            }.map {
                it.compra
            }

            _precioProducto.value=it.filter {
                it.tienda==numTienda
            }.map {
                it.valor
            }

            _fechaVenta.value=it.filter {
                it.tienda==numTienda
            }.map {
                it.fecha.toString()
            }


        }
    }

}