package com.virtualworld.agendadeventas.core.source

import android.util.Log


import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualword3d.salesregister.Data.Entity.Vendido
import javax.inject.Inject



class RepoFirebase @Inject constructor()
{

    var totalProductos = 0
    var contadorProductosExportados = 0
    val userId = Firebase.auth.currentUser?.email

    fun startSecion(email: String, password: String, mensaje: (Mensajes) -> Unit)
    {
        mensaje(Mensajes.CARGANDO)

        try
        {
            Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

                if (it.isSuccessful)
                {
                    mensaje(Mensajes.BIEN)
                } else
                {
                    mensaje(Mensajes.ERROR)
                }
            }
        } catch (ex: Exception)
        {
            mensaje(Mensajes.ERROR)
        }
    }

    fun createUser(email: String, password: String, mensaje: (Mensajes) -> Unit)
    {
        mensaje(Mensajes.CARGANDO)

        try
        {
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        mensaje(Mensajes.BIEN)
                    } else
                    {
                        mensaje(Mensajes.ERROR)
                    }
                }
        } catch (ex: Exception)
        {
            mensaje(Mensajes.ERROR)
        }
    }

    fun closeSecion(mensaje: (Mensajes) -> Unit)
    {
        mensaje(Mensajes.CARGANDO)
        Firebase.auth.signOut()
        mensaje(Mensajes.BIEN)
    }

    fun importSales(mensaje: (Mensajes) -> Unit, listSales: (List<Vendido>) -> Unit)
    {
        val _listSales : MutableList<Vendido> = mutableListOf()
        mensaje(Mensajes.CARGANDO)
        val userId = Firebase.auth.currentUser?.email

        FirebaseFirestore.getInstance().collection("fire-agenda-venta").document("$userId")
            .collection("Productos").get().addOnCompleteListener { taksProductos ->

                if (!taksProductos.result.metadata.isFromCache)
                {
                    if (taksProductos.isSuccessful)
                    {
                        taksProductos.addOnSuccessListener { fireProductos ->

                            for (document in fireProductos)
                            {

                                val vendido: Vendido=document.toObject(Vendido::class.java)
                                Log.d("", document.data.toString())
                                _listSales.add(vendido)
                            }
                            listSales(_listSales)
                            mensaje(Mensajes.BIEN)
                        }
                    } else
                    {
                        mensaje(Mensajes.ERROR)
                    }
                } else
                {
                    mensaje(Mensajes.ERROR)
                }
            }
    }

    fun exportSales(listSales: List<Vendido>,
                    message: (Mensajes) -> Unit,
                    connection: (Boolean) -> Unit)
    {

        message(Mensajes.CARGANDO)
        totalProductos = listSales.size
        contadorProductosExportados = 0



        listSales.map {
            formatExport(connection,
                         message,
                         it.idbd,
                         it.idprod,
                         it.nombre,
                         it.compra,
                         it.valor,
                         it.tienda,
                         it.unidades,
                         it.fecha
            )
        }


    }

    private fun formatExport(conexion: (Boolean) -> Unit,
                             message: (Mensajes) -> Unit,
                             idbd: Long,
                             idprod: Long,
                             nombre: String,
                             compra: Long,
                             valor: Long,
                             tienda: Int,
                             unidades: Int,
                             fecha: Long)
    {


        val productoExportar = mutableMapOf<String, Any>()

        productoExportar["idbd"] = idbd
        productoExportar["idprod"] = idprod
        productoExportar["nombre"] = nombre
        productoExportar["compra"] = compra
        productoExportar["valor"] = valor
        productoExportar["tienda"] = tienda
        productoExportar["unidades"] = unidades
        productoExportar["fecha"] = fecha


        FirebaseFirestore.getInstance().collection("fire-agenda-venta").document("$userId")
            .collection("Productos").document(productoExportar["idbd"].toString())
            .set(productoExportar).addOnCompleteListener {


                if (it.isSuccessful)
                {
                    conexion(true)
                    contadorProductosExportados++
                    Log.e("efecto", contadorProductosExportados.toString())
                    if (contadorProductosExportados == totalProductos)
                    {
                        message(Mensajes.BIEN)
                    }
                } else
                {
                    message(Mensajes.ERROR)
                    Log.e("efecto", "incompleto")
                    FirebaseFirestore.getInstance().terminate()
                }

            }


    }


}
