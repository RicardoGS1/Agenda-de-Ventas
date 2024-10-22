package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import com.virtualworld.agendadeventas.core.source.RepoFirebase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ExportAllDataUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val repoFirebase: RepoFirebase
) {

    suspend fun invoke(userId: String): Flow<ScreenUiState> {

        re


        val bs = localRepository.getAllProductsRoom()
            .first { it is NetworkResponseState.Error || it is NetworkResponseState.Success }

        val bc = localRepository.getAllSold()
            .first { it is NetworkResponseState.Error || it is NetworkResponseState.Success }

        if (bs is NetworkResponseState.Error || bc is NetworkResponseState.Error) {
            return flowOf(ScreenUiState.ERROR)

        } else {

            if (bs is NetworkResponseState.Loading || bc is NetworkResponseState.Loading) {
                return flowOf(ScreenUiState.LOADING)
            }
            if (bs is NetworkResponseState.Success && bc is NetworkResponseState.Success) {

                println("todos success enviando")

                val response =
                    repoFirebase.exportSoldRoomListToFirestore(bc.result, bs.result, userId)
                if (response is NetworkResponseState.Success) {
                    return flowOf(ScreenUiState.OK)
                } else {
                    return flowOf(ScreenUiState.ERROR)
                }


            }
        }
       return
        }

    }




