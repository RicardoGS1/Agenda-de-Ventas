package com.virtualword3d.salesregister.ID

import android.content.Context
import androidx.room.Room
import com.virtualword3d.salesregister.Data.Dao.SoldDao
import com.virtualword3d.salesregister.Data.DataBases.VendidoDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object VendidoDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): VendidoDataBase
    {
        return Room.databaseBuilder(
            appContext,
            VendidoDataBase::class.java,
            "vendido.db"
        ).build()
    }

    @Provides
    fun provideVendidoDao(database: VendidoDataBase): SoldDao
    {
        return database.VendidoDao()
    }
}