package com.virtualword3d.salesregister.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import kotlinx.coroutines.flow.Flow


@Dao
interface VendidoDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoldRoomList(soldRoomList: List<SoldRoom>)

    @Insert
    fun addProductoVendido(vararg vendido: SoldRoom)

    @Query("SELECT * FROM  vendidos WHERE   idbd = (SELECT MAX(idbd)  FROM vendidos)")
    fun maxId(): SoldRoom

    @Query("SELECT * FROM vendidos ORDER BY idbd")
    fun getAll(): List<SoldRoom>

    @Query("SELECT * FROM vendidos WHERE fecha BETWEEN CASE WHEN :dateStart IS NULL THEN 0 ELSE :dateStart END AND CASE WHEN :dateEnd IS NULL THEN 9223372036854775807 ELSE :dateEnd END ORDER BY idbd")
    fun getAllSoldFromTo(dateStart: Long?, dateEnd: Long?): Flow<List<SoldRoom>>

    @Query("DELETE FROM vendidos")
    suspend fun cleanAll():Int

}