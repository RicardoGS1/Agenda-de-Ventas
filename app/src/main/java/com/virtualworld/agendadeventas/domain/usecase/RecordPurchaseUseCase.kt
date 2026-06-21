package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.repository.LocalRepository
import javax.inject.Inject

class RecordPurchaseUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    suspend operator fun invoke(
        productId: Long,
        productName: String,
        unitCost: Float,
        storeId: Int,
        units: Int,
        deductedFromCash: Boolean,
    ) {
        localRepository.recordPurchase(
            productId = productId,
            productName = productName,
            unitCost = unitCost,
            storeId = storeId,
            units = units,
            deductedFromCash = deductedFromCash,
        )
    }
}
