package com.virtualworld.agendadeventas.domain.UseCase

import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.SoldForStore
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepocitory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSoldAllUseCase  @Inject constructor(private val localRepocitory: LocalRepocitory) {

    fun getSoldAll(): Flow<NetworkResponseState<List<SoldRoom>>> {

        return localRepocitory.getAllSold().map { response ->

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