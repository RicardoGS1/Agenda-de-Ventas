package com.virtualword3d.salesregister.ID

import android.content.Context
import androidx.room.Room
import com.virtualword3d.salesregister.Data.Dao.TiendaDao
import com.virtualword3d.salesregister.Data.DataBases.TiendasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TiendasDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TiendasDatabase
    {
        return Room.databaseBuilder(
            appContext,
            TiendasDatabase::class.java,
            "loggingggggjxgg.db"
        ).build()
    }


    @Provides
    fun provideTiendasDao(database: TiendasDatabase): TiendaDao
    {
        return database.tiendaDao()
    }
}