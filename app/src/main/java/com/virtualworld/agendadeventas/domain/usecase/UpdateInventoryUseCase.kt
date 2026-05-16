package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.repository.LocalRepository
import javax.inject.Inject

class UpdateInventoryUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend fun addStock(productId: Long, amount: Int) {
        if (amount > 0) localRepository.adjustInventory(productId, amount)
    }

    suspend fun discountStock(productId: Long, amount: Int) {
        if (amount > 0) localRepository.adjustInventory(productId, -amount)
    }
}
