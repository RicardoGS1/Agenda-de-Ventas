package com.virtualworld.agendadeventas.core.model

data class ProductStoreCore(
    val idProduct: Int,
    val nombre: String,
    val compra: Float,
    val venta: Float,
    val tieneInventario: Boolean = false,
    val inventario: Int = 0,
    val imagePath: String? = null,
)
