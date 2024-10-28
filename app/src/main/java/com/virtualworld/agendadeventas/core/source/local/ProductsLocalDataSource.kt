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

import com.virtualworld.agendadeventas.core.Dao.ProductoDao
import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import kotlinx.coroutines.flow.Flow


import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */

@Singleton
class ProductsLocalDataSource @Inject constructor(private val productoDao: ProductoDao) {


    fun getAllProducts(): Flow<List<ProductRoom>> {

        return productoDao.getAllProduct()
    }


    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }



    suspend fun addProduct(productRoom: ProductRoom): NetworkResponseState<Unit> {

        return try {

            productoDao.insertAll(productRoom)


            NetworkResponseState.Success(Unit)
        }catch(e:Exception){
            NetworkResponseState.Error(e)
        }

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




    suspend fun deletedAllProduct() {
        productoDao.cleanAll()
    }

    suspend fun insertListProduct(result: List<ProductRoom>) {

        productoDao.insertProductRoomList(result)
    }


}
