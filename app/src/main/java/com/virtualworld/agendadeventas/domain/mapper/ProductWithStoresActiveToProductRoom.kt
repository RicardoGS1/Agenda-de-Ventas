package com.virtualworld.agendadeventas.domain.mapper

import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive

fun ProductWithStoresActive.toProductRoom(): ProductRoom {
    val listMap = storesValues.associate { it.idStore.toInt() to it.value }
    val invMap = storesValues.associate {
        it.idStore.toInt() to (it.inventoryValue.ifEmpty { "0" }.toIntOrNull() ?: 0)
    }

    return ProductRoom(
        id = idProduct.toLong(),
        nombre = productName,
        compra = productCost.toFloat(),
        venta1 = listMap[1]?.toFloat() ?: 0f,
        venta2 = listMap[2]?.toFloat() ?: 0f,
        venta3 = listMap[3]?.toFloat() ?: 0f,
        venta4 = listMap[4]?.toFloat() ?: 0f,
        venta5 = listMap[5]?.toFloat() ?: 0f,
        tieneInventario = tieneInventario,
        inventario1 = invMap[1] ?: 0,
        inventario2 = invMap[2] ?: 0,
        inventario3 = invMap[3] ?: 0,
        inventario4 = invMap[4] ?: 0,
        inventario5 = invMap[5] ?: 0,
        imagePath = imagePath,
    )
}
