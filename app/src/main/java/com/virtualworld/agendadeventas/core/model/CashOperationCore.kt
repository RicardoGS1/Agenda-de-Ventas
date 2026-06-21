package com.virtualworld.agendadeventas.core.model

enum class CashOperationType {
    SALE,
    DEPOSIT,
    WITHDRAWAL,
}

data class CashOperationCore(
    val id: Long,
    val type: CashOperationType,
    val amount: Float,
    val signedAmount: Float,
    val description: String,
    val fecha: Long,
    val units: Int? = null,
)
