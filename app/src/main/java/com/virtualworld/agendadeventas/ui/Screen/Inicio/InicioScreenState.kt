package com.virtualworld.agendadeventas.ui.Screen.Inicio

data class InicioScreenState(


    val listResumenVenta: List<ResumenVenta> = emptyList(),



    )

data class ResumenVenta(


    val tienda : String="Tiendax",
    val ganancia : Long=0,
    val unidades : Int =0,


)

