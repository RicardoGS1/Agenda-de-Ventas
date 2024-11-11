package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllStoreUseCase@Inject constructor(private val repositoryLocal: LocalRepository) {

    fun getAllStores(): Flow<NetworkResponseState<List<StoreRoom>>> {
        return repositoryLocal.getAllStores()


    }

}