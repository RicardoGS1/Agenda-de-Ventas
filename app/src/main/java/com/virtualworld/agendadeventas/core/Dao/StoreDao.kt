package com.virtualworld.agendadeventas.core.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tiendas: StoreRoom)

    @Update
    suspend fun updateStore(tiendas: List<StoreRoom>)

    @Query("SELECT * FROM tiendas")
    fun getAllStoresFlow(): Flow<List<StoreRoom>>


    @Query("SELECT * FROM tiendas")
    fun getAll(): List<StoreRoom>

}