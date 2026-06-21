package com.virtualworld.agendadeventas.core.model

data class StoreCashSummaryCore(
    val storeId: Int,
    val storeName: String,
    val salesTotal: Float,
    val depositsTotal: Float,
    val withdrawalsTotal: Float,
) {
    val balance: Float get() = salesTotal + depositsTotal - withdrawalsTotal
}
