package com.virtualworld.agendadeventas.core.source.local

import android.os.Handler
import android.os.Looper

import com.virtualword3d.salesregister.Data.Dao.SoldDao
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import kotlinx.coroutines.flow.Flow

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

    suspend fun addProductsSold(listaVenta: List<SoldRoom>) {

//        withContext(Dispatchers.IO) {
            soldDao.insertSoldRoomList(listaVenta)

//        }
    }



}