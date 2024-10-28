package com.virtualworld.agendadeventas.ui.screen.add

data class ProductUiState(
    val productName: String = "",
    val productCost: String = "",
    val storeValues: Map<Int, String> = emptyMap()
)

