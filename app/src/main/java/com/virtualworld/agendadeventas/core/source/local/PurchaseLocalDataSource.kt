package com.virtualworld.agendadeventas.core.source.local

import com.virtualworld.agendadeventas.core.Dao.PurchaseDao
import com.virtualworld.agendadeventas.core.entity.PurchaseRoom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseLocalDataSource @Inject constructor(
    private val purchaseDao: PurchaseDao,
) {

    fun getByStoreAndProduct(storeId: Int, productId: Long): Flow<List<PurchaseRoom>> =
        purchaseDao.getByStoreAndProduct(storeId, productId)

    fun getByStore(storeId: Int): Flow<List<PurchaseRoom>> =
        purchaseDao.getByStore(storeId)

    fun getAll(): Flow<List<PurchaseRoom>> =
        purchaseDao.getAll()

    suspend fun insert(purchase: PurchaseRoom): Long =
        purchaseDao.insert(purchase)

    suspend fun insertAll(purchases: List<PurchaseRoom>) {
        purchases.forEach { purchaseDao.insert(it) }
    }

    suspend fun cleanAll() {
        purchaseDao.cleanAll()
    }
}
