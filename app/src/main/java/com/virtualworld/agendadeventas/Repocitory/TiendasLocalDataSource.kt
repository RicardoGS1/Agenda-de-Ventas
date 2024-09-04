package com.virtualword3d.salesregister.Repocitory

import android.os.Handler
import android.os.Looper
import com.virtualword3d.salesregister.Data.Dao.TiendaDao
import com.virtualword3d.salesregister.Data.Entity.Tiendas
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TiendasLocalDataSource @Inject constructor (private val tiendaDao: TiendaDao) {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }


    fun updateTiendas(id: Long, nombre: String, activa: Boolean) {

        executorService.execute {
            tiendaDao.updateTiendas(Tiendas(id, nombre, activa ) )
        }
    }

    fun getTiendas(callback: (List<Tiendas>) -> Unit) {

        executorService.execute {
            var tiendas = tiendaDao.getAll()


            tiendas.ifEmpty {

                tiendas = listOf(
                    Tiendas(1,"Tienda1",true),
                    Tiendas(2,"Tienda2",false),
                    Tiendas(3,"Tienda3",false),
                    Tiendas(4,"Tienda4",false),
                    Tiendas(5,"Tienda5",false)
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