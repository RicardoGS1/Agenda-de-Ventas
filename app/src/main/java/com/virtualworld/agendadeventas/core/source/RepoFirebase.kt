package com.virtualworld.agendadeventas.core.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException


import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.core.entity.ScreenUiState
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RepoFirebase @Inject constructor() {

    var totalProductos = 0
    var contadorProductosExportados = 0
    val userId = Firebase.auth.currentUser?.email


    suspend fun authenticateUser(
        email: String,
        password: String,
        isNewUser: Boolean
    ): NetworkResponseState<String> =

        withContext(Dispatchers.IO) {
            try {
                if (isNewUser) {
                    Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                } else {
                    Firebase.auth.signInWithEmailAndPassword(email, password).await()
                }
                NetworkResponseState.Success(email)
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                NetworkResponseState.Error(e)
            } catch (e: FirebaseAuthUserCollisionException) {
                NetworkResponseState.Error(e)
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }


    suspend fun closeSecion(): NetworkResponseState<String> =
        withContext(Dispatchers.IO) {
            try {
                Firebase.auth.signOut()
                NetworkResponseState.Success("")
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }


    suspend fun exportSoldRoomListToFirestore(
        soldRoomList: List<SoldRoom>,
        liProductRoom: List<ProductRoom>,
        userId: String
    ): NetworkResponseState<Unit> =

        withContext(Dispatchers.IO) {
            try {
                val collectionRef = FirebaseFirestore.getInstance().collection("fire-agenda-venta")
                val documentRef = collectionRef.document(userId)

                // Borrar datos existentes
                documentRef.get().await().let { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        documentSnapshot.reference.delete().await()
                    }
                }

                val batch = FirebaseFirestore.getInstance().batch()

                soldRoomList.forEach { soldRoom ->
                    val soldRoomData = hashMapOf(
                        "idbd" to soldRoom.idbd,
                        "idprod" to soldRoom.idprod,
                        "nombre" to soldRoom.nombre,
                        "compra" to soldRoom.compra,
                        "valor" to soldRoom.valor,
                        "tienda" to soldRoom.tienda,
                        "unidades" to soldRoom.unidades,
                        "fecha" to soldRoom.fecha
                    )
                    val documentRefForSoldRoom =
                        documentRef.collection("soldRoomList").document(soldRoom.idbd.toString())
                    batch.set(documentRefForSoldRoom, soldRoomData)
                }

                batch.commit().await()

                val batch2 = FirebaseFirestore.getInstance().batch()

                liProductRoom.forEach { productRoom ->
                    val productRoomData = hashMapOf(
                        "id" to productRoom.id,
                        "compra" to productRoom.nombre,
                        "nombre" to productRoom.compra,
                        "venta1" to productRoom.venta1,
                        "venta2" to productRoom.venta2,
                        "venta3" to productRoom.venta3,
                        "venta4" to productRoom.venta4,
                        "venta5" to productRoom.venta5
                    )
                    val documentRefForSoldRoom =
                        documentRef.collection("productRoomList").document(productRoom.id.toString())
                    batch2.set(documentRefForSoldRoom, productRoomData)
                }



                batch2.commit().await()

                NetworkResponseState.Success(Unit)
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }

    suspend fun importSoldAndProductRoomListsFromFirestore(userId: String): NetworkResponseState<Pair<List<SoldRoom>, List<ProductRoom>>> =
        withContext(Dispatchers.IO) {
            try {
                val collectionRef = FirebaseFirestore.getInstance().collection("fire-agenda-venta")
                val documentRef = collectionRef.document(userId)

                // Obtener soldRoomList
                val soldRoomListDeferred = async {
                    documentRef.collection("soldRoomList").get().await().documents.map { document ->
                        SoldRoom(
                            idbd = document.getLong("idbd") ?: 0,
                            idprod = document.getLong("idprod") ?: 0,
                            nombre = document.getString("nombre") ?:"",
                            compra = document.getLong("compra") ?: 0,
                            valor = document.getLong("valor") ?: 0,
                            tienda = document.getLong("tienda")?.toInt() ?: 0,
                            unidades = document.getLong("unidades")?.toInt() ?: 0,
                            fecha = document.getLong("fecha") ?: 0
                        )
                    }
                }

                // Obtener liProductRoom
                val liProductRoomDeferred = async {
                    documentRef.collection("productRoomList").get().await().documents.map { document ->
                        ProductRoom(
                            id = document.getLong("id") ?: 0,
                            nombre = document.getString("nombre") ?: "",
                            compra = document.getLong("compra") ?: 0,
                            venta1 = document.getLong("venta1") ?: 0,
                            venta2 = document.getLong("venta2") ?: 0,
                            venta3 = document.getLong("venta3") ?: 0,
                            venta4 = document.getLong("venta4") ?: 0,
                            venta5 = document.getLong("venta5") ?: 0
                        )}
                }

                // Esperar a que ambas listas se obtengan
                val soldRoomList = soldRoomListDeferred.await()
                val liProductRoom = liProductRoomDeferred.await()

                // Retornar ambas listas en un Pair
                NetworkResponseState.Success(Pair(soldRoomList, liProductRoom))
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }

    suspend fun importProductRoomListFromFirestore(userId: String): NetworkResponseState<List<ProductRoom>> =
        withContext(Dispatchers.IO) {
            try {
                val collectionRef = FirebaseFirestore.getInstance().collection("fire-agenda-venta")
                val documentRef = collectionRef.document(userId)

                val liProductRoom = documentRef.collection("productRoomList").get().await().documents.map { document ->
                    ProductRoom(
                        id = document.getLong("id") ?: 0,
                        nombre = document.getString("nombre") ?: "",
                        compra = document.getLong("compra") ?: 0,
                        venta1 = document.getLong("venta1") ?: 0,
                        venta2 = document.getLong("venta2") ?: 0,
                        venta3 = document.getLong("venta3") ?: 0,
                        venta4 = document.getLong("venta4") ?: 0,
                        venta5 = document.getLong("venta5") ?: 0
                    )
                }

                NetworkResponseState.Success(liProductRoom)
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }


    suspend fun importSoldRoomListFromFirestore(userId: String): NetworkResponseState<List<SoldRoom>> =

        withContext(Dispatchers.IO) {

            try {
                // Obtener la colección de Firestore
                val collectionRef = FirebaseFirestore.getInstance()
                    .collection("fire-agenda-venta")
                    .document(userId)
                    .collection("soldRoomList")

                // Obtener los documentos
                val querySnapshot = collectionRef.get().await()

                // Convertir los documentos a una lista de SoldRoom
                val soldRoomList = querySnapshot.documents.map { document ->
                    SoldRoom(
                        idbd = document.getLong("idbd") ?: 0,
                        idprod = document.getLong("idprod") ?: 0,
                        nombre = document.getString("nombre") ?: "",
                        compra = document.getLong("compra") ?: 0,
                        valor = document.getLong("valor") ?: 0,
                        tienda = document.getLong("tienda")?.toInt() ?: 0,
                        unidades = document.getLong("unidades")?.toInt() ?: 0,
                        fecha = document.getLong("fecha") ?: 0
                    )
                }

                // Borrar los datos existentes en la base de datos
                //  vendidoDao.deleteAllSoldRoom()

                // Insertar los nuevos datos en la base de datos
                // vendidoDao.insertSoldRoomList(soldRoomList)


println(soldRoomList)
                NetworkResponseState.Success(soldRoomList)
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }




}


/*

fun exportSales(listSales: List<SoldRoom>) {

   // message(Mensajes.CARGANDO)
    totalProductos = listSales.size
    contadorProductosExportados = 0



    listSales.map {
        formatExport(
            connection,
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

private fun formatExport(
    conexion: (Boolean) -> Unit,
    message: (Mensajes) -> Unit,
    idbd: Long,
    idprod: Long,
    nombre: String,
    compra: Long,
    valor: Long,
    tienda: Int,
    unidades: Int,
    fecha: Long
) {


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


            if (it.isSuccessful) {
                conexion(true)
                contadorProductosExportados++
                Log.e("efecto", contadorProductosExportados.toString())
                if (contadorProductosExportados == totalProductos) {
                    message(Mensajes.BIEN)
                }
            } else {
                message(Mensajes.ERROR)
                Log.e("efecto", "incompleto")
                FirebaseFirestore.getInstance().terminate()
            }

        }


}

//----------------------------------------------------------------------------------------------


fun importSales(mensaje: (Mensajes) -> Unit, listSales: (List<SoldRoom>) -> Unit) {
    val _listSales: MutableList<SoldRoom> = mutableListOf()
    mensaje(Mensajes.CARGANDO)
    val userId = Firebase.auth.currentUser?.email

    FirebaseFirestore.getInstance().collection("fire-agenda-venta").document("$userId")
        .collection("Productos").get().addOnCompleteListener { taksProductos ->

            if (!taksProductos.result.metadata.isFromCache) {
                if (taksProductos.isSuccessful) {
                    taksProductos.addOnSuccessListener { fireProductos ->

                        for (document in fireProductos) {

                            val vendido: SoldRoom = document.toObject(SoldRoom::class.java)
                            Log.d("", document.data.toString())
                            _listSales.add(vendido)
                        }
                        listSales(_listSales)
                        mensaje(Mensajes.BIEN)
                    }
                } else {
                    mensaje(Mensajes.ERROR)
                }
            } else {
                mensaje(Mensajes.ERROR)
            }
        }
}


 */








