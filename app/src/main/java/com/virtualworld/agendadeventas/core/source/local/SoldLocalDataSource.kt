package com.virtualworld.agendadeventas.core.source.local

import android.os.Handler
import android.os.Looper

import com.virtualword3d.salesregister.Data.Dao.SoldDao
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoldLocalDataSource @Inject constructor(private val soldDao: SoldDao) {

    fun getAllSoldFromTo(dateStart: Long?, dateEnd: Long?): Flow<List<SoldRoom>> =
        soldDao.getAllSoldFromTo(dateStart, dateEnd)


    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

   suspend fun cleanSales() {

            soldDao.cleanAll()


    }

    suspend fun addProductoVendido(listaVenta: List<SoldRoom>) {

        withContext(Dispatchers.IO) {
            soldDao.insertSoldRoomList(listaVenta)

        }
    }


    fun maxId(callback: (Long) -> Unit) {

        executorService.execute {
            val logs = soldDao.maxId()
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
            val listvendidos = soldDao.getAll()


            mainThreadHandler.post { callback(listvendidos) }
        }

    }

    fun getAllVendidosPorTienda(callback: (List<SoldRoom>) -> Unit) {
        executorService.execute {
            val listvendidos = soldDao.getAll()



            mainThreadHandler.post { callback(listvendidos) }
        }


    }


}