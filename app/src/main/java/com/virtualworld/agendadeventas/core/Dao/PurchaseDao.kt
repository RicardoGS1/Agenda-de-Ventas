package com.virtualworld.agendadeventas.core.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.virtualworld.agendadeventas.core.entity.PurchaseRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    @Insert
    suspend fun insert(purchase: PurchaseRoom): Long

    @Query(
        """
        SELECT * FROM compras
        WHERE tienda = :storeId AND idprod = :productId
        ORDER BY fecha DESC, id DESC
        """,
    )
    fun getByStoreAndProduct(storeId: Int, productId: Long): Flow<List<PurchaseRoom>>

    @Query("SELECT * FROM compras WHERE tienda = :storeId ORDER BY fecha DESC, id DESC")
    fun getByStore(storeId: Int): Flow<List<PurchaseRoom>>

    @Query("SELECT * FROM compras ORDER BY fecha DESC, id DESC")
    fun getAll(): Flow<List<PurchaseRoom>>

    @Query("DELETE FROM compras")
    suspend fun cleanAll(): Int
}
