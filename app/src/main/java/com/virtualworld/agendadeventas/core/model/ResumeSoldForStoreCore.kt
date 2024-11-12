package com.virtualworld.agendadeventas.core.model

data class ResumeSoldForStoreCore(
    val compra : Float,
    val valor : Float,
    val unidades : Int,
    val idTienda : Int,
    val tienda : String,
)
