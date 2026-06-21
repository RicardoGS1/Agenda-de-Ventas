package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.StoreCashSummaryCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoreCashSummariesUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    operator fun invoke(): Flow<NetworkResponseState<List<StoreCashSummaryCore>>> =
        localRepository.getStoreCashSummaries()
}
