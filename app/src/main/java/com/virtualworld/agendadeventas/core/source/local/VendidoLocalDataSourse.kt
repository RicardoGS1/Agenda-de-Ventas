package com.virtualworld.agendadeventas.core.source.local

import android.os.Handler
import android.os.Looper

import com.virtualword3d.salesregister.Data.Dao.VendidoDao
import com.virtualword3d.salesregister.Data.Entity.Vendido
import com.virtualworld.agendadeventas.core.Model.DataSellCore
import kotlinx.coroutines.flow.Flow

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VendidoLocalDataSourse @Inject constructor(private val vendidoDao: VendidoDao) {

    fun getDatosVentaEntreFechas(dateStart:Long?,dateEnd:Long?): Flow<List<Vendido>> {

        println(dateStart.toString()+"    "+dateEnd.toString())
        return vendidoDao.getDatosVentaEntreFechas(dateStart, dateEnd)

    }


    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

    fun cleanSales(callback: (Int) -> Unit) {


        executorService.execute {

            val response = vendidoDao.cleanAll()

            mainThreadHandler.post {
                callback(response)
            }
        }

    }

    fun addProductoVendido(listaVenta: List<Vendido>) {
        for (i in listaVenta.indices) {
            executorService.execute {

                vendidoDao.addProductoVendido(listaVenta[i])
            }
        }
    }


    fun maxId(callback: (Long) -> Unit) {

        executorService.execute {
            val logs = vendidoDao.maxId()
            val maxidbd: Long

            if (logs != null) maxidbd = logs.idbd
            else maxidbd = 0

            mainThreadHandler.post {
                callback(maxidbd)
            }
        }
    }


    fun getAllVendidos(callback: (List<Vendido>) -> Unit) {

        executorService.execute {
            val listvendidos = vendidoDao.getAll()


            mainThreadHandler.post { callback(listvendidos) }
        }

    }

    fun getAllVendidosPorTienda(callback: (List<Vendido>) -> Unit) {
        executorService.execute {
            val listvendidos = vendidoDao.getAll()



            mainThreadHandler.post { callback(listvendidos) }
        }


    }


}