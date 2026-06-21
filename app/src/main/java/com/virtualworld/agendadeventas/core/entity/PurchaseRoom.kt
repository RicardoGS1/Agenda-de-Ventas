package com.virtualworld.agendadeventas.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compras")
data class PurchaseRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idprod: Long = 0,
    val nombre: String = "",
    val compra: Float = 0f,
    val tienda: Int = 0,
    val unidades: Int = 0,
    val fecha: Long = 0,
    val deductedFromCash: Boolean = true,
)
