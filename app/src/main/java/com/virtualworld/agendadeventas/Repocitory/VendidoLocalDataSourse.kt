package com.virtualword3d.salesregister.Repocitory

import android.os.Handler
import android.os.Looper

import com.virtualword3d.salesregister.Data.Dao.VendidoDao
import com.virtualword3d.salesregister.Data.Entity.Vendido

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VendidoLocalDataSourse @Inject constructor(private val VendidoDao: VendidoDao)
{

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

    fun cleanSales(callback: (Int) -> Unit){


            executorService.execute {

                val response = VendidoDao.cleanAll()

                mainThreadHandler.post {
                    callback(response)
                }
            }

    }

    fun addProductoVendido(listaVenta: List<Vendido>)
    {
        for (i in listaVenta.indices)
        {
            executorService.execute {

                VendidoDao.addProductoVendido(listaVenta[i])
            }
        }
    }


    fun maxId(callback: (Long) -> Unit)
    {

        executorService.execute {
            val logs = VendidoDao.maxId()
            val maxidbd: Long

            if (logs != null) maxidbd = logs.idbd
            else maxidbd = 0

            mainThreadHandler.post {
                callback(maxidbd)
            }
        }
    }


    fun getAllVendidos(callback: (List<Vendido>) -> Unit)
    {

        executorService.execute {
            val listvendidos = VendidoDao.getAll()


            mainThreadHandler.post { callback(listvendidos) }
        }

    }

    fun getAllVendidosPorTienda(callback: (List<Vendido>) -> Unit)
    {
        executorService.execute {
            val listvendidos = VendidoDao.getAll()



            mainThreadHandler.post { callback(listvendidos) }
        }


    }


}