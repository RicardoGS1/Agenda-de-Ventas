package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import com.virtualworld.agendadeventas.core.source.FirebaseRepository
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExportAllDataUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val firebaseRepository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String): ScreenUiState {

        val productsResult = localRepository.getAllProducts()
            .first { it is NetworkResponseState.Error || it is NetworkResponseState.Success }

        val soldResult = localRepository.getAllSold()
            .first { it is NetworkResponseState.Error || it is NetworkResponseState.Success }

        when {
            productsResult is NetworkResponseState.Error || soldResult is NetworkResponseState.Error -> {
                return (ScreenUiState.ERROR)
            }

            productsResult is NetworkResponseState.Success && soldResult is NetworkResponseState.Success -> {
                val response = firebaseRepository.exportDataRoomToFirestore(
                    soldResult.result,
                    productsResult.result,
                    userId
                )
                return (if (response is NetworkResponseState.Success) ScreenUiState.OK else ScreenUiState.ERROR)
            }
            else -> return ScreenUiState.ERROR
        }
    }

}



