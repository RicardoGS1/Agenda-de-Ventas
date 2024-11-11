package com.virtualworld.agendadeventas.ui.screen.common

data class ProductUiState(
    val productName: String = "",
    val productCost: String = "",
    val storeValues: Map<Int, String> = emptyMap()
)

