package com.virtualworld.agendadeventas.core.Model

data class ResumeSoldForStoreCore(
    val compra : Long,
    val valor : Long,
    val unidades : Int,
    val idTienda : Int,
    val tienda : String,
)
