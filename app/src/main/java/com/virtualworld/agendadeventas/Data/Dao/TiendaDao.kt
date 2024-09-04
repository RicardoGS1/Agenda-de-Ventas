package com.virtualword3d.salesregister.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.virtualword3d.salesregister.Data.Entity.Tiendas

@Dao
interface TiendaDao {



    @Insert
    fun insertAll(vararg tiendas: Tiendas)

    @Update
    fun updateTiendas(vararg tiendas: Tiendas)


    @Query("SELECT * FROM tiendas")
    fun getAll(): List<Tiendas>

}