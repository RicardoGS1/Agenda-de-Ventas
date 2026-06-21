package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.PurchaseForStoreCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductPurchaseHistoryUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    operator fun invoke(storeId: Int, productId: Long): Flow<NetworkResponseState<List<PurchaseForStoreCore>>> =
        localRepository.getPurchasesForProduct(storeId, productId).map { response ->
            when (response) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Error -> NetworkResponseState.Error(response.exception)
                is NetworkResponseState.Success -> NetworkResponseState.Success(response.result)
            }
        }
}
