package com.virtualworld.agendadeventas.core.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException


import com.google.firebase.firestore.FirebaseFirestore
import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.id.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {


    suspend fun authenticateUser(
        email: String,
        password: String,
        isNewUser: Boolean
    ): NetworkResponseState<String> =

        withContext(ioDispatcher) {
            try {
                if (isNewUser) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
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
        withContext(ioDispatcher) {
            try {
                firebaseAuth.signOut()
                NetworkResponseState.Success("")
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }


    suspend fun exportDataRoomToFirestore(
        soldRoomList: List<SoldRoom>,
        liProductRoom: List<ProductRoom>,
        userId: String
    ): NetworkResponseState<Unit> =

        withContext(ioDispatcher) {
            try {
                val collectionRef = firebaseFirestore.collection("fire-agenda-venta")
                val documentRef = collectionRef.document(userId)


                val querySnapshot = documentRef.collection("soldRoomList").get().await()
                val querySnapshot2 = documentRef.collection("productRoomList").get().await()

                querySnapshot.documents.forEach { document ->
                    document.reference.delete().await()
                }

                querySnapshot2.documents.forEach { document ->
                    document.reference.delete().await()
                }


                val batch = firebaseFirestore.batch()

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

                val batch2 = firebaseFirestore.batch()

                liProductRoom.forEach { productRoom ->
                    val productRoomData = hashMapOf(
                        "id" to productRoom.id,
                        "compra" to productRoom.compra,
                        "nombre" to productRoom.nombre,
                        "venta1" to productRoom.venta1,
                        "venta2" to productRoom.venta2,
                        "venta3" to productRoom.venta3,
                        "venta4" to productRoom.venta4,
                        "venta5" to productRoom.venta5
                    )
                    val documentRefForSoldRoom =
                        documentRef.collection("productRoomList")
                            .document(productRoom.id.toString())
                    batch2.set(documentRefForSoldRoom, productRoomData)
                }



                batch2.commit().await()





                NetworkResponseState.Success(Unit)
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }



    suspend fun importProductRoomListFromFirestore(userId: String): NetworkResponseState<List<ProductRoom>> =
        withContext(ioDispatcher) {
            try {
                // Obtener la colección de Firestore
                val collectionRef = firebaseFirestore
                    .collection("fire-agenda-venta")
                    .document(userId)
                    .collection("productRoomList")

                // Obtener los documentos
                val querySnapshot = collectionRef.get().await()

                val liProductRoom = querySnapshot.documents.map { document ->
                    ProductRoom(
                        id = document.getLong("id") ?: 0,
                        nombre = document.getString("nombre") ?: "",
                        compra = document.getDouble("compra")?.toFloat() ?: 0f,
                        venta1 = document.getDouble("venta1")?.toFloat() ?: 0f,
                        venta2 = document.getDouble("venta2")?.toFloat() ?: 0f,
                        venta3 = document.getDouble("venta3")?.toFloat() ?: 0f,
                        venta4 = document.getDouble("venta4") ?.toFloat() ?: 0f,
                        venta5 = document.getDouble("venta5") ?.toFloat() ?: 0f,
                    )
                }

                println(liProductRoom)

                NetworkResponseState.Success(liProductRoom)
            } catch (e: Exception) {
                NetworkResponseState.Error(e)
            }
        }


    suspend fun importSoldRoomListFromFirestore(userId: String): NetworkResponseState<List<SoldRoom>> =

        withContext(ioDispatcher) {

            try {
                // Obtener la colección de Firestore
                val collectionRef = firebaseFirestore
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
                        compra = document.getDouble("compra")?.toFloat() ?: 0f,
                        valor = document.getDouble("valor")?.toFloat() ?: 0f,
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








