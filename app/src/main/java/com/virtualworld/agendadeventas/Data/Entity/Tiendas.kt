package com.virtualword3d.salesregister.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tiendas")
data class Tiendas(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val nombre: String,
    val activa: Boolean,

    )