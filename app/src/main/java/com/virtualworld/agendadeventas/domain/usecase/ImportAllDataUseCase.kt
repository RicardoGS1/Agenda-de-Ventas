package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.core.source.FirebaseRepository
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import javax.inject.Inject

class ImportAllDataUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val firebaseRepository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String): ScreenUiState {

        val productRoomList =
            firebaseRepository.importProductRoomListFromFirestore(userId)

        val soldRoomList =
            firebaseRepository.importSoldRoomListFromFirestore(userId)


        when {
            soldRoomList is NetworkResponseState.Success && productRoomList is NetworkResponseState.Success -> {

                println("eliminando bd")
                localRepository.deleteAllSales()
                localRepository.insertListSales(soldRoomList.result)

                localRepository.deleteAllProducts()
                localRepository.insertListProducts(productRoomList.result)

                return ScreenUiState.OK

            }

            soldRoomList is NetworkResponseState.Error || productRoomList is NetworkResponseState.Error -> {
                return ScreenUiState.ERROR
            }

            else -> return ScreenUiState.ERROR
        }

    }

}