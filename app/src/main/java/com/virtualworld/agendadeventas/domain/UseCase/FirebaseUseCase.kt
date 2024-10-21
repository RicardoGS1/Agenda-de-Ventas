package com.virtualword3d.salesregister.CasoUso

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.source.RepoFirebase
import javax.inject.Inject

class FirebaseUseCase @Inject constructor(private val repoFirebase: RepoFirebase)
{

    suspend fun authenticateUser(email: String, password: String, isNewUser: Boolean):NetworkResponseState<String>
    {
      return  repoFirebase.authenticateUser(email, password, isNewUser)
    }

    suspend fun closeSecion():NetworkResponseState<String>
    {
        return repoFirebase.closeSecion()
    }

    suspend fun exportSoldRoomListToFirestore(listSales: List<SoldRoom>, liProductRoom: List<ProductRoom>,userId: String):NetworkResponseState<Unit>

    {
         return repoFirebase.exportSoldRoomListToFirestore(listSales,liProductRoom,userId)
    }

    suspend fun importProductRoomListFromFirestore(userId: String):NetworkResponseState<List<ProductRoom>>
    {
        return repoFirebase.importProductRoomListFromFirestore(userId)
    }


    suspend fun importSoldRoomListFromFirestore(userId: String):NetworkResponseState<List<SoldRoom>>
    {
        return repoFirebase.importSoldRoomListFromFirestore(userId)
    }


    fun createUser(email: String, password: String, mensaje: (ScreenUiState) -> Unit)
    {
      //  repoFirebase.createUser(email, password, mensaje)
    }




    // fun exportSoldRoomListToFirestore(listSales: List<SoldRoom>, i
  //  {
       // repoFirebase.exportSoldRoomListToFirestore(listSales)
  //  }


}