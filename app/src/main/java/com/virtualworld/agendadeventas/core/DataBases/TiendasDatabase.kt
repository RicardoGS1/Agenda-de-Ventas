package com.virtualword3d.salesregister.Data.DataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtualword3d.salesregister.Data.Dao.TiendaDao
import com.virtualword3d.salesregister.Data.Entity.StoreRoom


@Database(entities = [StoreRoom::class], version = 1, exportSchema = false)
abstract class TiendasDatabase : RoomDatabase() {
    abstract fun tiendaDao(): TiendaDao
}