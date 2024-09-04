package com.virtualword3d.salesregister.Screen.Inicio

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.virtualword3d.salesregister.Repocitory.ProductoLocalDataSource
import com.virtualword3d.salesregister.Repocitory.TiendasLocalDataSource
import com.virtualword3d.salesregister.Repocitory.VendidoLocalDataSourse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ViewModelInicio @Inject constructor(private val productoRepo: ProductoLocalDataSource,
                                          private val tiendasRepo: TiendasLocalDataSource,
                                          private val vendidoRepo: VendidoLocalDataSourse) :
    ViewModel()
{


    private val _dateDesde = MutableLiveData<String>("")
    val dateDesde: LiveData<String> = _dateDesde

    private val _dateHasta = MutableLiveData<String>("")
    val dateHasta: LiveData<String> = _dateHasta


    fun changeDateDesde(date: String)
    {
        _dateDesde.value = date
    }

    fun changeDateHasta(date: String)
    {
        _dateHasta.value = date
    }


    private val _nombreTienda1 = MutableLiveData<String>("")
    val nombreTienda1: LiveData<String> = _nombreTienda1

    private val _nombreTienda2 = MutableLiveData<String>("")
    val nombreTienda2: LiveData<String> = _nombreTienda2

    private val _nombreTienda3 = MutableLiveData<String>("")
    val nombreTienda3: LiveData<String> = _nombreTienda3

    private val _nombreTienda4 = MutableLiveData<String>("")
    val nombreTienda4: LiveData<String> = _nombreTienda4

    private val _nombreTienda5 = MutableLiveData<String>("")
    val nombreTienda5: LiveData<String> = _nombreTienda5

    private val _unidadesTienda1 = MutableLiveData<Int>(0)
    val unidadesTienda1: LiveData<Int> = _unidadesTienda1

    private val _unidadesTienda2 = MutableLiveData<Int>(0)
    val unidadesTienda2: LiveData<Int> = _unidadesTienda2

    private val _unidadesTienda3 = MutableLiveData<Int>(0)
    val unidadesTienda3: LiveData<Int> = _unidadesTienda3

    private val _unidadesTienda4 = MutableLiveData<Int>(0)
    val unidadesTienda4: LiveData<Int> = _unidadesTienda4

    private val _unidadesTienda5 = MutableLiveData<Int>(0)
    val unidadesTienda5: LiveData<Int> = _unidadesTienda5

    fun getTiendas()
    {

        tiendasRepo.getTiendas {

            if (it[0].activa) _nombreTienda1.value = it[0].nombre
            else _nombreTienda1.value = ""

            if (it[1].activa) _nombreTienda2.value = it[1].nombre
            else _nombreTienda2.value = ""


            if (it[2].activa) _nombreTienda3.value = it[2].nombre
            else _nombreTienda3.value = ""


            if (it[3].activa) _nombreTienda4.value = it[3].nombre
            else _nombreTienda4.value = ""


            if (it[4].activa) _nombreTienda5.value = it[4].nombre
            else _nombreTienda5.value = ""

        }
    }


    private val _listaVendidosFecha = MutableLiveData<List<Long>>()
    val listaVendidosFecha: LiveData<List<Long>> = _listaVendidosFecha

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUnidades()
    {

        //  val dateDesdeFormat = _dateDesde.value?.get(0)

        // val currentDateMillis: Long = _dateDesde.value.timeInMillis

        if (_dateDesde.value != "" && _dateHasta.value != "")
        {

            val fechaDesde =
                LocalDate.parse(_dateDesde.value, DateTimeFormatter.ofPattern("d/M/yyyy"))
            val fechaHasta =
                LocalDate.parse(_dateHasta.value, DateTimeFormatter.ofPattern("d/M/yyyy"))




            vendidoRepo.getAllVendidos {


                val productosVendidosFecha = it.filter {

                    val fechaventa =
                        LocalDate.parse(it.fecha, DateTimeFormatter.ofPattern("d/M/yyyy"))

                    var com1 = fechaDesde.compareTo(fechaventa)

                    var com2 = fechaHasta.compareTo(fechaventa)



                    com1 <= 0 && com2 >= 0

                }

                Log.e("efecto", productosVendidosFecha.toString())

                _unidadesTienda1.value = 0
                _unidadesTienda2.value = 0
                _unidadesTienda3.value = 0
                _unidadesTienda4.value = 0
                _unidadesTienda5.value = 0


                productosVendidosFecha.map {


                    when (it.tienda)
                    {

                        0 ->
                        {
                            _unidadesTienda1.value = _unidadesTienda1.value?.plus(it.unidades)
                            Log.e("efecto", _unidadesTienda1.value.toString())
                        }

                        1 ->
                        {
                            _unidadesTienda2.value = _unidadesTienda2.value?.plus(it.unidades)
                            Log.e("efecto", _unidadesTienda2.value.toString())
                        }

                        2 ->
                        {
                            _unidadesTienda3.value = _unidadesTienda3.value?.plus(it.unidades)
                            Log.e("efecto", _unidadesTienda3.value.toString())
                        }

                        3 ->
                        {
                            _unidadesTienda4.value = _unidadesTienda4.value?.plus(it.unidades)
                            Log.e("efecto", _unidadesTienda4.value.toString())
                        }

                        4 ->
                        {
                            _unidadesTienda5.value = _unidadesTienda5.value?.plus(it.unidades)
                            Log.e("efecto", _unidadesTienda5.value.toString())
                        }


                        else ->
                        {
                            Log.e("efecto", "ninguna".toString())
                        }
                    }

                }


            }

        }

        //   var localDate:LocalDate


        //  it.forEach{vendido->

        //   localDate = LocalDate.parse(vendido.fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }


    private val _gananciaTienda1 = MutableLiveData<Long>(0)
    val gananciaTienda1: LiveData<Long> = _gananciaTienda1

    private val _gananciaTienda2 = MutableLiveData<Long>(0)
    val gananciaTienda2: LiveData<Long> = _gananciaTienda2

    private val _gananciaTienda3 = MutableLiveData<Long>(0)
    val gananciaTienda3: LiveData<Long> = _gananciaTienda3

    private val _gananciaTienda4 = MutableLiveData<Long>(0)
    val gananciaTienda4: LiveData<Long> = _gananciaTienda4

    private val _gananciaTienda5 = MutableLiveData<Long>(0)
    val gananciaTienda5: LiveData<Long> = _gananciaTienda5


    @RequiresApi(Build.VERSION_CODES.O)
    fun getGanancia()
    {

        if (_dateDesde.value != "" && _dateHasta.value != "")
        {

            val fechaDesde =
                LocalDate.parse(_dateDesde.value, DateTimeFormatter.ofPattern("d/M/yyyy"))
            val fechaHasta =
                LocalDate.parse(_dateHasta.value, DateTimeFormatter.ofPattern("d/M/yyyy"))

            vendidoRepo.getAllVendidos {


                val productosVendidosFecha = it.filter {

                    val fechaventa =
                        LocalDate.parse(it.fecha, DateTimeFormatter.ofPattern("d/M/yyyy"))

                    var com1 = fechaDesde.compareTo(fechaventa)

                    var com2 = fechaHasta.compareTo(fechaventa)



                    com1 <= 0 && com2 >= 0

                }

                Log.e("efecto", productosVendidosFecha.toString())

                _gananciaTienda1.value = 0
                _gananciaTienda2.value = 0
                _gananciaTienda3.value = 0
                _gananciaTienda4.value = 0
                _gananciaTienda5.value = 0


                productosVendidosFecha.map {


                    when (it.tienda)
                    {

                        0 ->
                        {
                            _gananciaTienda1.value =
                                _gananciaTienda1.value?.plus((it.valor - it.compra) * it.unidades)
                            Log.e("efecto", _gananciaTienda1.value.toString())
                        }

                        1 ->
                        {
                            _gananciaTienda2.value =
                                _gananciaTienda2.value?.plus((it.valor - it.compra) * it.unidades)
                            Log.e("efecto", _gananciaTienda2.value.toString())
                        }

                        2 ->
                        {
                            _gananciaTienda3.value =
                                _gananciaTienda3.value?.plus((it.valor - it.compra) * it.unidades)
                            Log.e("efecto", _gananciaTienda3.value.toString())
                        }

                        3 ->
                        {
                            _gananciaTienda4.value =
                                _gananciaTienda4.value?.plus((it.valor - it.compra) * it.unidades)
                            Log.e("efectoprueba", _gananciaTienda4.value.toString()+"   "+it.valor )
                        }

                        4 ->
                        {
                            _gananciaTienda5.value =
                                _gananciaTienda5.value?.plus((it.valor - it.compra) * it.unidades)
                            Log.e("efecto", _gananciaTienda5.value.toString())
                        }


                        else ->
                        {
                            Log.e("efecto", "ninguna".toString())
                        }
                    }

                }


            }

        }

    }


}





