package com.virtualworld.agendadeventas.core.source.local

import android.os.Handler
import android.os.Looper
import com.virtualword3d.salesregister.Data.Dao.TiendaDao

import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoresLocalDataSource @Inject constructor(private val tiendaDao: TiendaDao) {


    fun getAllStores(): Flow<List<StoreRoom>>  {
        return tiendaDao.getAllStoresFlow()
    }






    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }



    fun updateTiendas(id: Long, nombre: String, activa: Boolean) {

        executorService.execute {
            tiendaDao.updateTiendas(StoreRoom(id, nombre, activa))
        }
    }


    fun getTiendas(callback: (List<StoreRoom>) -> Unit) {


        executorService.execute {
            var tiendas = tiendaDao.getAll()


            tiendas.ifEmpty {

                tiendas = listOf(
                    StoreRoom(1, "Tienda1", true),
                    StoreRoom(2, "Tienda2", false),
                    StoreRoom(3, "Tienda3", false),
                    StoreRoom(4, "Tienda4", false),
                    StoreRoom(5, "Tienda5", false)
                )

                tiendaDao.insertAll(tiendas[0])
                tiendaDao.insertAll(tiendas[1])
                tiendaDao.insertAll(tiendas[2])
                tiendaDao.insertAll(tiendas[3])
                tiendaDao.insertAll(tiendas[4])
            }

            mainThreadHandler.post { callback(tiendas) }
        }

    }


}