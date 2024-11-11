package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.source.FirebaseRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository)
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