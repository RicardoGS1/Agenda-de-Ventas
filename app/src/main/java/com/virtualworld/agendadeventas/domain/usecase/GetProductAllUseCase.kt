package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductAllUseCase @Inject constructor(private val localRepocitory: LocalRepository) {

    operator fun invoke (): Flow<NetworkResponseState<List<ProductRoom>>> {
        return localRepocitory.getAllProducts()
    }




}