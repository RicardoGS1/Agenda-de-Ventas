package com.virtualword3d.salesregister.Screen.Agregar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.core.source.local.ProductoLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ViewModelAgregar  @Inject constructor(private val productoRepo: ProductoLocalDataSource, private val tiendasRepo: TiendasLocalDataSource) : ViewModel()
{
    private val _respuestaError= MutableLiveData<ScreenUiState>(ScreenUiState.NEUTRAL)
    val respuestaError : LiveData<ScreenUiState> = _respuestaError

    private val _nombreProducto= MutableLiveData<String>("")
    val nombreProducto : LiveData<String> = _nombreProducto

    private val _precioCompra= MutableLiveData<String>("")
    val precioCompra : LiveData<String> = _precioCompra


    private val _PrecioTienda1= MutableLiveData<String>("")
    val precioTienda1 : LiveData<String> = _PrecioTienda1

    private val _PrecioTienda2= MutableLiveData<String>("")
    val precioTienda2 : LiveData<String> = _PrecioTienda2

    private val _PrecioTienda3= MutableLiveData<String>("")
    val precioTienda3 : LiveData<String> = _PrecioTienda3

    private val _PrecioTienda4= MutableLiveData<String>("")
    val precioTienda4 : LiveData<String> = _PrecioTienda4

    private val _PrecioTienda5= MutableLiveData<String>("")
    val precioTienda5 : LiveData<String> = _PrecioTienda5

    private val _ActivaTienda1= MutableLiveData<Boolean>()
    val activaTienda1 : LiveData<Boolean> = _ActivaTienda1

    private val _ActivaTienda2= MutableLiveData<Boolean>()
    val activaTienda2 : LiveData<Boolean> = _ActivaTienda2

    private val _ActivaTienda3= MutableLiveData<Boolean>()
    val activaTienda3 : LiveData<Boolean> = _ActivaTienda3

    private val _ActivaTienda4= MutableLiveData<Boolean>()
    val activaTienda4 : LiveData<Boolean> = _ActivaTienda4

    private val _ActivaTienda5= MutableLiveData<Boolean>()
    val activaTienda5 : LiveData<Boolean> = _ActivaTienda5

    private val _nombreTienda1= MutableLiveData<String>()
    val nombreTienda1 : LiveData<String> = _nombreTienda1

    private val _nombreTienda2= MutableLiveData<String>()
    val nombreTienda2 : LiveData<String> = _nombreTienda2

    private val _nombreTienda3= MutableLiveData<String>()
    val nombreTienda3 : LiveData<String> = _nombreTienda3

    private val _nombreTienda4= MutableLiveData<String>()
    val nombreTienda4 : LiveData<String> = _nombreTienda4

    private val _nombreTienda5= MutableLiveData<String>()
    val nombreTienda5 : LiveData<String> = _nombreTienda5




    fun OnChangedNombreProducto(nomProducto: String) {
        _nombreProducto.value = nomProducto
    }

    fun OnChangedPrecioCompra(preProducto: String) {
        _precioCompra.value = ValidarNumeroDecimal(preProducto)
    }

    fun OnChangedPrecioTienda1(preTienda1: String) {
        _PrecioTienda1.value = ValidarNumeroDecimal(preTienda1)
    }

    fun OnChangedPrecioTienda2(preTienda2: String) {
        _PrecioTienda2.value = ValidarNumeroDecimal(preTienda2)
    }

    fun OnChangedPrecioTienda3(preTienda3: String) {
        _PrecioTienda3.value = ValidarNumeroDecimal(preTienda3)
    }

    fun OnChangedPrecioTienda4(preTienda4: String) {
        _PrecioTienda4.value = ValidarNumeroDecimal(preTienda4)

    }

    fun OnChangedPrecioTienda5(preTienda5: String) {

        _PrecioTienda5.value = ValidarNumeroDecimal(preTienda5)
    }



    fun reiniciarValores(){
        OnChangedNombreProducto("")
        OnChangedPrecioCompra("")
        OnChangedPrecioTienda1("")
        OnChangedPrecioTienda2("")
        OnChangedPrecioTienda3("")
        OnChangedPrecioTienda4("")
        OnChangedPrecioTienda5("")
    }

    fun ValidarNumeroDecimal(preTienda:String) : String {

        val filteredChars = preTienda.filterIndexed {
                index, c -> c in "0123456789" ||   (c == '.' && preTienda.indexOf('.') == index)
        }

        return filteredChars
    }

    fun setProducto() {

        val error =productoRepo.addProducto(_nombreProducto.value!!, _precioCompra.value!!, _PrecioTienda1.value!!, _PrecioTienda2.value!!, _PrecioTienda3.value!!, _PrecioTienda4.value!!, _PrecioTienda5.value!!)

        if(error==0)
            controlMensaje(ScreenUiState.OK)
        else
            controlMensaje(ScreenUiState.ERROR)

    }

    fun getTiendas(){
        tiendasRepo.getTiendas {


            if(!it[0].activa){
                _ActivaTienda1.value=false
                _PrecioTienda1.value=0.toString()

            }else
                _nombreTienda1.value=it[0].nombre

            if(!it[1].activa){
                _ActivaTienda2.value=false
                _PrecioTienda2.value=0.toString()

            }else
                _nombreTienda2.value=it[1].nombre


            if(!it[2].activa){
                _ActivaTienda3.value=false
                _PrecioTienda3.value=0.toString()

            }else
                _nombreTienda3.value=it[2].nombre


            if(!it[3].activa){
                _ActivaTienda4.value=false
                _PrecioTienda4.value=0.toString()

            }else
                _nombreTienda4.value=it[3].nombre


            if(!it[4].activa){
                _ActivaTienda5.value=false
                _PrecioTienda5.value=0.toString()

            }else
                _nombreTienda5.value=it[4].nombre



        }
    }

    fun controlMensaje(mensaje: ScreenUiState){

        _respuestaError.value=mensaje


    }

}