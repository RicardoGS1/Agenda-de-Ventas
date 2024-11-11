package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.StoresActiveCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject




class GetStoresActiveUseCase @Inject constructor(private val repositoryLocal: LocalRepository) {

    fun getStoresActive(): Flow<NetworkResponseState<List<StoresActiveCore>>>{
        return repositoryLocal.getStoresActive()


    }

}
