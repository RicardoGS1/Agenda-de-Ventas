package com.virtualworld.agendadeventas.core.DataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.virtualworld.agendadeventas.core.Dao.CashTransactionDao
import com.virtualworld.agendadeventas.core.Dao.PurchaseDao
import com.virtualworld.agendadeventas.core.entity.CashTransactionRoom
import com.virtualworld.agendadeventas.core.entity.PurchaseRoom
import com.virtualword3d.salesregister.Data.Dao.SoldDao
import com.virtualword3d.salesregister.Data.Entity.SoldRoom


@Database(
    entities = [SoldRoom::class, CashTransactionRoom::class, PurchaseRoom::class],
    version = 3,
    exportSchema = false,
)
abstract class VendidoDataBase : RoomDatabase() {
    abstract fun VendidoDao(): SoldDao
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun purchaseDao(): PurchaseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cash_transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        storeId INTEGER NOT NULL,
                        amount REAL NOT NULL,
                        type TEXT NOT NULL,
                        fecha INTEGER NOT NULL,
                        note TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS compras (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        idprod INTEGER NOT NULL,
                        nombre TEXT NOT NULL,
                        compra REAL NOT NULL,
                        tienda INTEGER NOT NULL,
                        unidades INTEGER NOT NULL,
                        fecha INTEGER NOT NULL,
                        deductedFromCash INTEGER NOT NULL DEFAULT 1
                    )
                    """.trimIndent(),
                )
            }
        }
    }
}