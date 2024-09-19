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

package com.virtualword3d.salesregister.Data.Dao

import androidx.room.*
import com.virtualword3d.salesregister.Data.Entity.ProductRoom


/**
 * Data access object to query the database.
 */
@Dao
interface ProductoDao {


    @Update
    fun update(vararg producto: ProductRoom)

    @Query("SELECT * FROM  productos WHERE   id = (SELECT MAX(id)  FROM productos)")
    fun maxId(): ProductRoom

    @Query("SELECT * FROM productos ORDER BY id")
    fun getAll(): List<ProductRoom>

    @Insert
    fun insertAll(vararg producto: ProductRoom)

    @Delete
    fun borrar(producto: ProductRoom): Int
}
