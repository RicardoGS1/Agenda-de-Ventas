package com.virtualworld.agendadeventas.core.DataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtualworld.agendadeventas.core.Dao.StoreDao
import com.virtualword3d.salesregister.Data.Entity.StoreRoom


@Database(entities = [StoreRoom::class], version = 1, exportSchema = false)
abstract class TiendasDatabase : RoomDatabase() {
    abstract fun tiendaDao(): StoreDao
}