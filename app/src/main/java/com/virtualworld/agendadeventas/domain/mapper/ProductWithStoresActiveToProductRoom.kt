package com.virtualworld.agendadeventas.domain.mapper

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive

fun ProductWithStoresActive.toProductRoom(): ProductRoom {
    val listMap = storesValues.associate { it.idStore.toInt() to it.value }

    return  ProductRoom(
        id = idProduct.toLong(),
        nombre = productName,
        compra = productCost.toLong(),
        venta1 = listMap[1]?.toLong() ?: 0,
        venta2 = listMap[2]?.toLong() ?: 0,
        venta3 = listMap[3]?.toLong() ?: 0,
        venta4 = listMap[4]?.toLong() ?: 0,
        venta5 = listMap[5]?.toLong() ?: 0
    )
}