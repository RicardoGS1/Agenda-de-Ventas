package com.virtualworld.agendadeventas.core.source.local

import com.virtualworld.agendadeventas.core.Dao.CashTransactionDao
import com.virtualworld.agendadeventas.core.entity.CashTransactionRoom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CashLocalDataSource @Inject constructor(
    private val cashTransactionDao: CashTransactionDao,
) {

    fun getTransactionsByStore(storeId: Int): Flow<List<CashTransactionRoom>> =
        cashTransactionDao.getByStore(storeId)

    fun getAllTransactions(): Flow<List<CashTransactionRoom>> =
        cashTransactionDao.getAll()

    suspend fun insertTransaction(transaction: CashTransactionRoom): Long =
        cashTransactionDao.insert(transaction)

    suspend fun insertAll(transactions: List<CashTransactionRoom>) {
        transactions.forEach { cashTransactionDao.insert(it) }
    }

    suspend fun cleanAll() {
        cashTransactionDao.cleanAll()
    }
}
