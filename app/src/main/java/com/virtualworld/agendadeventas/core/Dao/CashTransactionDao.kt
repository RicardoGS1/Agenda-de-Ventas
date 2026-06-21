package com.virtualworld.agendadeventas.core.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.virtualworld.agendadeventas.core.entity.CashTransactionRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface CashTransactionDao {

    @Insert
    suspend fun insert(transaction: CashTransactionRoom): Long

    @Query("SELECT * FROM cash_transactions WHERE storeId = :storeId ORDER BY fecha DESC, id DESC")
    fun getByStore(storeId: Int): Flow<List<CashTransactionRoom>>

    @Query("SELECT * FROM cash_transactions ORDER BY fecha DESC, id DESC")
    fun getAll(): Flow<List<CashTransactionRoom>>

    @Query("DELETE FROM cash_transactions")
    suspend fun cleanAll(): Int
}
