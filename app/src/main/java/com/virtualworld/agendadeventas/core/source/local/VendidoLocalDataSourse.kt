package com.virtualworld.agendadeventas.core.source.local

import android.os.Handler
import android.os.Looper

import com.virtualword3d.salesregister.Data.Dao.VendidoDao
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VendidoLocalDataSourse @Inject constructor(private val vendidoDao: VendidoDao) {

    fun getAllSoldFromTo(dateStart: Long?, dateEnd: Long?): Flow<List<SoldRoom>> =
        vendidoDao.getAllSoldFromTo(dateStart, dateEnd)


    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

   suspend fun cleanSales() {

            vendidoDao.cleanAll()


    }

    suspend fun addProductoVendido(listaVenta: List<SoldRoom>) {

        withContext(Dispatchers.IO) {
            vendidoDao.insertSoldRoomList(listaVenta)

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


    fun getAllVendidos(callback: (List<SoldRoom>) -> Unit) {

        executorService.execute {
            val listvendidos = vendidoDao.getAll()


            mainThreadHandler.post { callback(listvendidos) }
        }

    }

    fun getAllVendidosPorTienda(callback: (List<SoldRoom>) -> Unit) {
        executorService.execute {
            val listvendidos = vendidoDao.getAll()



            mainThreadHandler.post { callback(listvendidos) }
        }


    }


}