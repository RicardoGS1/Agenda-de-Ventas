package com.virtualword3d.salesregister.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.virtualword3d.salesregister.Data.Entity.Vendido


@Dao
interface VendidoDao {



    @Insert
    fun addProductoVendido(vararg vendido: Vendido)

    @Query("SELECT * FROM  vendidos WHERE   idbd = (SELECT MAX(idbd)  FROM vendidos)")
    fun maxId(): Vendido

    @Query("SELECT * FROM vendidos ORDER BY idbd")
    fun getAll(): List<Vendido>

    @Query("DELETE FROM vendidos")
    fun cleanAll():Int

}