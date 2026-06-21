package com.virtualworld.agendadeventas.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cash_transactions")
data class CashTransactionRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val storeId: Int = 0,
    val amount: Float = 0f,
    val type: String = TYPE_DEPOSIT,
    val fecha: Long = 0,
    val note: String = "",
) {
    companion object {
        const val TYPE_DEPOSIT = "DEPOSIT"
        const val TYPE_WITHDRAWAL = "WITHDRAWAL"
    }
}
