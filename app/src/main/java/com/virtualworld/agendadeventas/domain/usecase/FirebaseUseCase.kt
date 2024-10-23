package com.virtualword3d.salesregister.CasoUso

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.source.FirebaseRepository
import javax.inject.Inject

class FirebaseUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository)
{

    suspend fun authenticateUser(email: String, password: String, isNewUser: Boolean):NetworkResponseState<String>
    {
      return  firebaseRepository.authenticateUser(email, password, isNewUser)
    }

    suspend fun closeSecion():NetworkResponseState<String>
    {
        return firebaseRepository.closeSecion()
    }

}