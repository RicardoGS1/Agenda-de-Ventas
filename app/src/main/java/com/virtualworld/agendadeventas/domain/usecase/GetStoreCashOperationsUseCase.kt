package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.CashOperationCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoreCashOperationsUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    operator fun invoke(storeId: Int): Flow<NetworkResponseState<List<CashOperationCore>>> =
        localRepository.getStoreCashOperations(storeId)
}
