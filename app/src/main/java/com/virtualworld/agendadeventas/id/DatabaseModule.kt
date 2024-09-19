package com.virtualword3d.salesregister.ID

import android.content.Context
import androidx.room.Room
import com.virtualword3d.salesregister.Data.Dao.ProductoDao
import com.virtualword3d.salesregister.Data.DataBases.ProductoDatabase


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ProductoDatabase
    {
        return Room.databaseBuilder(
            appContext,
            ProductoDatabase::class.java,
            "logging.db"
        ).build()
    }

    @Provides
    fun provideLogDao(database: ProductoDatabase): ProductoDao
    {
        return database.logDao()
    }






}