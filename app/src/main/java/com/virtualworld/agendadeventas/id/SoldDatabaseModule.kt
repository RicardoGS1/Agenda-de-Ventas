package com.virtualworld.agendadeventas.id

import android.content.Context
import androidx.room.Room
import com.virtualworld.agendadeventas.core.Dao.CashTransactionDao
import com.virtualworld.agendadeventas.core.Dao.PurchaseDao
import com.virtualword3d.salesregister.Data.Dao.SoldDao
import com.virtualworld.agendadeventas.core.DataBases.VendidoDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SoldDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): VendidoDataBase
    {
        return Room.databaseBuilder(
            appContext,
            VendidoDataBase::class.java,
            "vendido.db"
        ).addMigrations(
            VendidoDataBase.MIGRATION_1_2,
            VendidoDataBase.MIGRATION_2_3,
        ).build()
    }

    @Provides
    fun provideVendidoDao(database: VendidoDataBase): SoldDao
    {
        return database.VendidoDao()
    }

    @Provides
    fun provideCashTransactionDao(database: VendidoDataBase): CashTransactionDao
    {
        return database.cashTransactionDao()
    }

    @Provides
    fun providePurchaseDao(database: VendidoDataBase): PurchaseDao
    {
        return database.purchaseDao()
    }
}