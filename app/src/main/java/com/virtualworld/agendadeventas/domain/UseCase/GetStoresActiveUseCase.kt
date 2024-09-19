package com.virtualworld.agendadeventas.domain.UseCase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.StoresActiveCore
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepocitory
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject




class GetStoresActiveUseCase @Inject constructor(private val repoLocal: LocalRepocitory) {

    fun GetTiendasActivas(): Flow<NetworkResponseState<List<StoresActiveCore>>>{
        return repoLocal.GetTiendasActivas()


    }

}
