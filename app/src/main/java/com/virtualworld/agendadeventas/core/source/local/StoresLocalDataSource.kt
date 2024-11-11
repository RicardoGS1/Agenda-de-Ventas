package com.virtualworld.agendadeventas.core.source.local

import android.os.Handler
import android.os.Looper
import com.virtualworld.agendadeventas.core.Dao.StoreDao

import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoresLocalDataSource @Inject constructor(private val storeDao: StoreDao) {


    fun getAllStores(): Flow<List<StoreRoom>> {
        return storeDao.getAllStoresFlow()
    }

    suspend fun updateStores(storeRoom: List<StoreRoom>)  {
        storeDao.updateStore(storeRoom)
    }


    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }


    fun getTiendas(callback: (List<StoreRoom>) -> Unit) {


        executorService.execute {
            var tiendas = storeDao.getAll()


            tiendas.ifEmpty {

                tiendas = listOf(
                    StoreRoom(1, "Tienda1", true),
                    StoreRoom(2, "Tienda2", false),
                    StoreRoom(3, "Tienda3", false),
                    StoreRoom(4, "Tienda4", false),
                    StoreRoom(5, "Tienda5", false)
                )

            }

            mainThreadHandler.post { callback(tiendas) }
        }

    }


}