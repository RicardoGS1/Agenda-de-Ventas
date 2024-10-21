package com.virtualword3d.salesregister.Data.Entity


data class ObjectVendido(var idbd: Long = 0,
                         var idprod: Long = 0,
                         val nombre: String = "",
                         val compra: Long = 0,
                         val valor: Long = 0,
                         var tienda: Int = 0,
                         val unidades: Int = 0,
                         val fecha: String = "")