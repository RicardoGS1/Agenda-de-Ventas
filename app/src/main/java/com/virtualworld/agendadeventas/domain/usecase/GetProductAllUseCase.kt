package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductAllUseCase @Inject constructor(private val localRepocitory: LocalRepository) {

    operator fun invoke (): Flow<NetworkResponseState<List<ProductRoom>>> {
        return localRepocitory.getAllProducts()
    }




}