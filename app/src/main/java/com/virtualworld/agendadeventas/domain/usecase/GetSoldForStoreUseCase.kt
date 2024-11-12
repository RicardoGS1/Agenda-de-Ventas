package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.SoldForStoreCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSoldForStoreUseCase @Inject constructor(private val localRepocitory: LocalRepository) {

    fun getSoldForStore(idStore: Int): Flow<NetworkResponseState<List<SoldForStoreCore>>> {

       return localRepocitory.getAllSoldForStore(idStore).map { response ->

            when (response) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Error -> NetworkResponseState.Error(response.exception)
                is NetworkResponseState.Success -> {


                    NetworkResponseState.Success(response.result)
                }
            }


        }
    }
}


