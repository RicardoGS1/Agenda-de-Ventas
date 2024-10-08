package com.virtualword3d.salesregister.CasoUso

import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.core.source.RepoFirebase
import javax.inject.Inject

class CasoUsoFirebase @Inject constructor(private val repoFirebase: RepoFirebase)
{

    fun startSecion(email: String, password: String, mensaje: (Mensajes) -> Unit)
    {
        repoFirebase.startSecion(email, password, mensaje)
    }

    fun createUser(email: String, password: String, mensaje: (Mensajes) -> Unit)
    {
        repoFirebase.createUser(email, password, mensaje)
    }

    fun closeSecion(mensaje: (Mensajes) -> Unit)
    {
        repoFirebase.closeSecion(mensaje)
    }

    fun importSales(mensaje: (Mensajes) -> Unit,
                    listSales: (List<SoldRoom>) ->Unit)
    {
        repoFirebase.importSales(mensaje,listSales)
    }

    fun exportSales(listSales: List<SoldRoom>,
                    message: (Mensajes) -> Unit,
                    conexion: (Boolean) -> Unit)
    {
        repoFirebase.exportSales(listSales, message, conexion)
    }


}