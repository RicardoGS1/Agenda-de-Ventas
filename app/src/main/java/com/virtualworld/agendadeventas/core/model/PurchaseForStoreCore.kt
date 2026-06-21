package com.virtualworld.agendadeventas.core.model

data class PurchaseForStoreCore(
    val id: Long = 0,
    val idprod: Long = 0,
    val nombre: String = "",
    val compra: Float = 0f,
    val unidades: Int = 0,
    val fecha: Long = 0,
    val deductedFromCash: Boolean = true,
    val imagePath: String? = null,
)
