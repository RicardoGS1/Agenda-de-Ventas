/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virtualworld.agendadeventas.core.source.local

import android.os.Handler
import android.os.Looper
import android.util.Log

import com.virtualword3d.salesregister.Data.Dao.ProductoDao
import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import kotlinx.coroutines.flow.Flow


import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */

//CLASE UNICA QUE SE LE INYECTA ProductoDao QUE CONTIENE LOS ACCSESOS A LA BD "productos"
@Singleton
class ProductoLocalDataSource @Inject constructor(private val productoDao: ProductoDao) {



   fun getAllProducts(): Flow<List<ProductRoom>> {

       return  productoDao.getAllProduct()
   }



    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }


    //AGREGA PRODUCTO A BD CON LOS VALORES PASADOS POR CUALQUIER CLASE QUE LO CONTENGA
    fun addProducto(nombre: String, compra: String, venta1: String, venta2: String, venta3: String, venta4: String, venta5: String ):Int {

        var error:Int=1

        var compraLong: Long = 0
        var venta1Long: Long = 0
        var venta2Long: Long = 0
        var venta3Long: Long = 0
        var venta4Long: Long = 0
        var venta5Long: Long = 0


        if (nombre != "" && compra != "") {
            compraLong = compra.toLong()

            if (venta1 != "")
                venta1Long = venta1.toLong()

            if (venta2 != "")
                venta2Long = venta2.toLong()

            if (venta3 != "")
                venta3Long = venta3.toLong()

            if (venta4 != "")
                venta4Long = venta4.toLong()

            if (venta5 != "")
                venta5Long = venta5.toLong()

            executorService.execute {
                val estadoTabla = productoDao.maxId()
                var id: Long = 0

                if (estadoTabla != null)
                    id = productoDao.maxId().id + 1

                Log.d("Agregando producto", ProductRoom(id, nombre, compraLong, venta1Long, venta2Long, venta3Long, venta4Long, venta5Long).toString())

                productoDao.insertAll(
                    ProductRoom(
                        id,
                        nombre,
                        compraLong,
                        venta1Long,
                        venta2Long,
                        venta3Long,
                        venta4Long,
                        venta5Long
                    )
                )

            }
            error=0

        }

        return error
    }

    fun updateProducto(producto: ProductRoom) {
        executorService.execute {
            productoDao.update(producto)
        }
    }


    fun getAllLogs(callback: (List<ProductRoom>) -> Unit) {
        executorService.execute {
            val logs = productoDao.getAll()
            mainThreadHandler.post { callback(logs) }
        }
    }


    fun removeProducto(producto: ProductRoom, callback: (Int) -> Unit) {
        executorService.execute {
            val respuesta = productoDao.borrar(producto)
            mainThreadHandler.post { callback(respuesta) }
        }
    }




    fun maxId(callback: (Long) -> Unit) {

        executorService.execute {
            val estadoTabla = productoDao.maxId()
            var logs: Long = 0

            if (estadoTabla != null)
                logs = productoDao.maxId().id

            mainThreadHandler.post { callback(logs) }
        }
    }

    suspend fun deletedAllProduct() {
       productoDao.cleanAll()
    }

    suspend fun insertListProduct(result: List<ProductRoom>) {

        productoDao.insertProductRoomList(result)
    }


}
