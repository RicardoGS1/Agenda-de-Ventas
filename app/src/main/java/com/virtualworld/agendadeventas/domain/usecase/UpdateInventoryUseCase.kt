package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.repository.LocalRepository
import javax.inject.Inject

class UpdateInventoryUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend fun addStock(productId: Long, storeId: Int, amount: Int) {
        if (amount > 0 && storeId in 1..5) {
            localRepository.adjustInventory(productId, storeId, amount)
        }
    }

    suspend fun discountStock(productId: Long, storeId: Int, amount: Int) {
        if (amount > 0 && storeId in 1..5) {
            localRepository.adjustInventory(productId, storeId, -amount)
        }
    }
}
