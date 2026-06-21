package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import javax.inject.Inject

class WithdrawCashFromStoreUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    suspend operator fun invoke(storeId: Int, amount: Float, note: String = ""): NetworkResponseState<Unit> =
        localRepository.withdrawCash(storeId, amount, note)
}
