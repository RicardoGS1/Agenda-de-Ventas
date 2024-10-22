package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import javax.inject.Inject

class DeleteAllProductUseCase @Inject constructor (private val localRepository: LocalRepository) {
    suspend fun deleteAllProductUseCase() {
        localRepository.deleteAllProducts()
    }
}