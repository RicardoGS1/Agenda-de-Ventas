package com.virtualworld.agendadeventas.domain.models


data class ProductWithStoresActive(
    val idProduct: String = "",
    val productName: String = "",
    val productCost: String = "",
    val storesValues: List<StoresValues> = listOf(StoresValues()),
    val tieneInventario: Boolean = false,
    val inventario: Int = 0,
)

data class StoresValues(
    val idStore: String = "",
    val nameStore: String = "",
    val value: String = ""
)

