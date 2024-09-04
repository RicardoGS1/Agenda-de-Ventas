package com.virtualword3d.salesregister.Screen.Editar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.virtualword3d.salesregister.Data.Entity.Producto
import com.virtualword3d.salesregister.Repocitory.ProductoLocalDataSource
import com.virtualword3d.salesregister.Repocitory.TiendasLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditarViewModel @Inject constructor(private val productoRepo: ProductoLocalDataSource, private val tiendasRepo: TiendasLocalDataSource) : ViewModel() {


    //PANTALLA EDITAR++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    var list_producto:List<Producto> = listOf()

    private val _nombreProducto = MutableLiveData<List<String>>()
    val nombreProducto: LiveData<List<String>> = _nombreProducto



    //VENTANA EDITAR++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    var id_producto_Editando:Long=-1

    private val _ventana_editar_enable = MutableLiveData<Boolean>()
    val ventana_editar_enable: LiveData<Boolean> = _ventana_editar_enable

    private val _nombreProductoEditar= MutableLiveData<String>()
    val nombreProductoEditar : LiveData<String> = _nombreProductoEditar

    private val _precioCompra= MutableLiveData<String>()
    val precioCompra : LiveData<String> = _precioCompra


    private val _PrecioTienda1= MutableLiveData<String>()
    val precioTienda1 : LiveData<String> = _PrecioTienda1

    private val _PrecioTienda2= MutableLiveData<String>()
    val precioTienda2 : LiveData<String> = _PrecioTienda2

    private val _PrecioTienda3= MutableLiveData<String>()
    val precioTienda3 : LiveData<String> = _PrecioTienda3

    private val _PrecioTienda4= MutableLiveData<String>()
    val precioTienda4 : LiveData<String> = _PrecioTienda4

    private val _PrecioTienda5= MutableLiveData<String>()
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


//VENTANA DETALLES++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private val _producto_detalles = MutableLiveData<Producto>()
    val producto_detalles: LiveData<Producto> = _producto_detalles

    private val _ventana_detalles_enable = MutableLiveData<Boolean>()
    val ventana_detalles_enable: LiveData<Boolean> = _ventana_detalles_enable


//VENTANA BORRAR++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private val _ventana_borrar_enable = MutableLiveData<Boolean>()
    val ventana_borrar_enable: LiveData<Boolean> = _ventana_borrar_enable

    private val _producto_borrar = MutableLiveData<Producto>()
    val producto_borrar: LiveData<Producto> = _producto_borrar



    fun ventanaDetalles(index: Int) {

        if(index!=-1){
            _producto_detalles.value=list_producto[index]



            _ventana_detalles_enable.value=true}else{
            _ventana_detalles_enable.value=false
        }

    }

    fun ventanaBorrar(index: Int) {

        if(index!=-1){
            _producto_borrar.value=list_producto[index]



            _ventana_borrar_enable.value=true}else{
            _ventana_borrar_enable.value=false
        }

    }

    fun ventanaEditar(index: Int){
        if(index!=-1){


            id_producto_Editando=list_producto[index].id
            OnChangedNombreProducto( list_producto[index].nombre)
            OnChangedPrecioCompra( list_producto[index].compra.toString())
            OnChangedPrecioTienda1( list_producto[index].venta1.toString())
            OnChangedPrecioTienda2( list_producto[index].venta2.toString())
            OnChangedPrecioTienda3( list_producto[index].venta3.toString())
            OnChangedPrecioTienda4( list_producto[index].venta4.toString())
            OnChangedPrecioTienda5( list_producto[index].venta5.toString())





            _ventana_editar_enable.value=true



        }else{
            _ventana_editar_enable.value=false
        }

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


    fun getProducto() {
        productoRepo.getAllLogs {

            _nombreProducto.value = it.map {
                it.nombre
            }

            list_producto=it
        }
    }

    fun borrarProducto(producto: Producto) {

        productoRepo.removeProducto(producto){
            getProducto()
        }




    }




    fun OnChangedNombreProducto(nomProducto: String) {
        _nombreProductoEditar.value = nomProducto
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

    fun ValidarNumeroDecimal(preTienda:String) : String {

        val filteredChars = preTienda.filterIndexed {
                index, c -> c in "0123456789" ||   (c == '.' && preTienda.indexOf('.') == index)
        }

        return filteredChars
    }

    fun updateProducto() {
      val prpducto:Producto=Producto(id_producto_Editando,_nombreProductoEditar.value!!, _precioCompra.value!!.toLong(), _PrecioTienda1.value!!.toLong(),  _PrecioTienda2.value!!.toLong(),   _PrecioTienda3.value!!.toLong(),  _PrecioTienda4.value!!.toLong(),   _PrecioTienda5.value!!.toLong())

        productoRepo.updateProducto(prpducto)

    }


}