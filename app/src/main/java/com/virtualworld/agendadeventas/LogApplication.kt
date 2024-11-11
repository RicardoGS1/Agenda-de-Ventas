package com.virtualworld.agendadeventas

import android.app.Application
import android.content.Context
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.core.Dao.StoreDao
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class LogApplication : Application() {

    @Inject
    lateinit var database: StoreDao


    override fun onCreate() {
        super.onCreate()


        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val esPrimeraEjecucion = prefs.getBoolean("first_run", true)


        if (esPrimeraEjecucion) {



            CoroutineScope(Dispatchers.IO).launch {


                for (i in 1..5) {
                    database.insertAll(StoreRoom(id = i.toLong(), nombre = "Store $i", activa = i==1))
                }

                with(prefs.edit()) {
                    putBoolean("first_run", false)
                    apply()
                }
            }
        }
    }


}