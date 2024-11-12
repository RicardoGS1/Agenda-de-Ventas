package com.virtualword3d.salesregister.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendidos")
data class SoldRoom(
    @PrimaryKey(autoGenerate = true)
    var idbd: Long = 0,
    var idprod: Long = 0,
    val nombre: String = "",
    val compra: Float = 0f,
    val valor: Float = 0f,
    var tienda: Int = 0,
    val unidades: Int = 0,
    val fecha: Long = 0,
    )