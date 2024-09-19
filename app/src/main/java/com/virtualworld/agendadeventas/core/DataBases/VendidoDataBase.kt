package com.virtualword3d.salesregister.Data.DataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtualword3d.salesregister.Data.Dao.VendidoDao
import com.virtualword3d.salesregister.Data.Entity.Vendido


@Database(entities = arrayOf(Vendido::class), version = 1, exportSchema = false)
abstract class VendidoDataBase : RoomDatabase() {
    abstract fun VendidoDao(): VendidoDao
}