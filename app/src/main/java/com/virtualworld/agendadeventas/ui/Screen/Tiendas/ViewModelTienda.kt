package com.virtualword3d.salesregister.Screen.Tiendas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelTienda @Inject constructor(private val tiendasRepo: TiendasLocalDataSource) : ViewModel()
{

    //VARIABLES DE NOMBRE TIENDAS
    private val _respuestaError= MutableLiveData<Mensajes>(Mensajes.NEUTRO)
    val respuestaError : LiveData<Mensajes> = _respuestaError

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


    //VARIABLES DE SHWIT
    private val _shwitTienda1 = MutableLiveData<Boolean>()
    val shwitTienda1: LiveData<Boolean> = _shwitTienda1

    private val _shwitTienda2 = MutableLiveData<Boolean>()
    val shwitTienda2: LiveData<Boolean> = _shwitTienda2

    private val _shwitTienda3 = MutableLiveData<Boolean>()
    val shwitTienda3: LiveData<Boolean> = _shwitTienda3

    private val _shwitTienda4 = MutableLiveData<Boolean>()
    val shwitTienda4: LiveData<Boolean> = _shwitTienda4

    private val _shwitTienda5 = MutableLiveData<Boolean>()
    val shwitTienda5: LiveData<Boolean> = _shwitTienda5


    fun OnChangedTienda1(nombreTienda: String) {
        _nombreTienda1.value = nombreTienda
    }

    fun OnChangedTienda2(nombreTienda: String) {
        _nombreTienda2.value = nombreTienda
    }

    fun OnChangedTienda3(nombreTienda: String) {
        _nombreTienda3.value = nombreTienda

    }

    fun OnChangedTienda4(nombreTienda: String) {
        _nombreTienda4.value = nombreTienda

    }

    fun OnChangedTienda5(nombreTienda: String) {
        _nombreTienda5.value = nombreTienda
    }

    fun OnChangedShwitTienda1(shwitTienda: Boolean) {
        _shwitTienda1.value = shwitTienda
    }

    fun OnChangedShwitTienda2(shwitTienda: Boolean) {
        _shwitTienda2.value = shwitTienda
    }

    fun OnChangedShwitTienda3(shwitTienda: Boolean) {
        _shwitTienda3.value = shwitTienda
    }

    fun OnChangedShwitTienda4(shwitTienda: Boolean) {
        _shwitTienda4.value = shwitTienda
    }

    fun OnChangedShwitTienda5(shwitTienda: Boolean) {
        _shwitTienda5.value = shwitTienda
    }


    fun getTiendas() {
        tiendasRepo.getTiendas() {
            OnChangedTienda1(it[0].nombre)
            OnChangedShwitTienda1(it[0].activa)
            OnChangedTienda2(it[1].nombre)
            OnChangedShwitTienda2(it[1].activa)
            OnChangedTienda3(it[2].nombre)
            OnChangedShwitTienda3(it[2].activa)
            OnChangedTienda4(it[3].nombre)
            OnChangedShwitTienda4(it[3].activa)
            OnChangedTienda5(it[4].nombre)
            OnChangedShwitTienda5(it[4].activa)
        }
    }

    fun setTiendas() {

        if (_nombreTienda1.value != "" && _nombreTienda2.value != "" && _nombreTienda3.value != "" && _nombreTienda4.value != "" && _nombreTienda5.value != "") {
            tiendasRepo.updateTiendas(1, _nombreTienda1.value!!, _shwitTienda1.value!!)
            tiendasRepo.updateTiendas(2, _nombreTienda2.value!!, _shwitTienda2.value!!)
            tiendasRepo.updateTiendas(3, _nombreTienda3.value!!, _shwitTienda3.value!!)
            tiendasRepo.updateTiendas(4, _nombreTienda4.value!!, _shwitTienda4.value!!)
            tiendasRepo.updateTiendas(5, _nombreTienda5.value!!, _shwitTienda5.value!!)
            controlMensaje(Mensajes.BIEN)
        } else {
            controlMensaje(Mensajes.ERROR)
        }
    }

    fun controlMensaje(mensaje: Mensajes){

        _respuestaError.value=mensaje


    }

}