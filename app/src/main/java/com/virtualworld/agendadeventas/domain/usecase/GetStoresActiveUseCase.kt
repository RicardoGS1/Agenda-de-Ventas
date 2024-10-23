package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.StoresActiveCore
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject




class GetStoresActiveUseCase @Inject constructor(private val repoLocal: LocalRepository) {

    fun GetTiendasActivas(): Flow<NetworkResponseState<List<StoresActiveCore>>>{
        return repoLocal.getStoresActive()


    }

}
