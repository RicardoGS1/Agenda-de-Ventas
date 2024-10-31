package com.virtualworld.agendadeventas.id

import android.content.Context
import androidx.room.Room
import com.virtualworld.agendadeventas.core.Dao.StoreDao
import com.virtualworld.agendadeventas.core.DataBases.TiendasDatabase
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
    fun provideTiendasDao(database: TiendasDatabase): StoreDao
    {
        return database.tiendaDao()
    }
}