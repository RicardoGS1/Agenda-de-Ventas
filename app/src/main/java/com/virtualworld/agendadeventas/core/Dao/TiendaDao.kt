package com.virtualword3d.salesregister.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface TiendaDao {



    @Insert
    fun insertAll(vararg tiendas: StoreRoom)

    @Update
    fun updateTiendas(vararg tiendas: StoreRoom)

    @Query("SELECT * FROM tiendas")
    fun getAllStoresFlow(): Flow<List<StoreRoom>>


    @Query("SELECT * FROM tiendas")
    fun getAll(): List<StoreRoom>

}