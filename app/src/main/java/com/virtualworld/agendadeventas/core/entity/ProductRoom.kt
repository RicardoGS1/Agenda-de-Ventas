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

package com.virtualword3d.salesregister.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represent the a table in the database.
 */
@Entity(tableName = "productos")
data class ProductRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val nombre: String = "",
    val compra: Long = 0,
    val venta1: Long = 0,
    val venta2: Long = 0,
    val venta3: Long = 0,
    val venta4: Long = 0,
    val venta5: Long = 0,
) {


}
