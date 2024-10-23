package com.virtualword3d.salesregister.Data.DataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtualword3d.salesregister.Data.Dao.SoldDao
import com.virtualword3d.salesregister.Data.Entity.SoldRoom


@Database(entities = arrayOf(SoldRoom::class), version = 1, exportSchema = false)
abstract class VendidoDataBase : RoomDatabase() {
    abstract fun VendidoDao(): SoldDao
}