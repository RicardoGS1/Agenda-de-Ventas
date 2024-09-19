package com.virtualword3d.salesregister.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.virtualword3d.salesregister.Data.Entity.Vendido
import com.virtualworld.agendadeventas.core.Model.DataSellCore
import kotlinx.coroutines.flow.Flow


@Dao
interface VendidoDao {



    @Insert
    fun addProductoVendido(vararg vendido: Vendido)

    @Query("SELECT * FROM  vendidos WHERE   idbd = (SELECT MAX(idbd)  FROM vendidos)")
    fun maxId(): Vendido

    @Query("SELECT * FROM vendidos ORDER BY idbd")
    fun getAll(): List<Vendido>

    @Query("SELECT * FROM vendidos WHERE fecha BETWEEN CASE WHEN :dateStart IS NULL THEN 0 ELSE :dateStart END AND CASE WHEN :dateEnd IS NULL THEN 9223372036854775807 ELSE :dateEnd END ORDER BY idbd")
    fun getDatosVentaEntreFechas(dateStart: Long?,dateEnd: Long?): Flow<List<Vendido>>

    @Query("DELETE FROM vendidos")
    fun cleanAll():Int

}