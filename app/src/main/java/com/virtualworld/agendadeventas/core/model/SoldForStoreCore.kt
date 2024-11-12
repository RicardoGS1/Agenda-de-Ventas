package com.virtualworld.agendadeventas.core.model

data class SoldForStoreCore(
    val idbd: Long = 0,
    val idprod: Long = 0,
    val nombre: String = "",
    val compra: Float = 0f,
    val valor: Float = 0f,
    val unidades: Int = 0,
    val fecha: Long = 0,
)
