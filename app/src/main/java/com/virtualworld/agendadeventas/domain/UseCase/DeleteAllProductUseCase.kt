package com.virtualworld.agendadeventas.domain.UseCase

import com.virtualworld.agendadeventas.core.Repocitory.LocalRepocitory
import javax.inject.Inject

class DeleteAllProductUseCase @Inject constructor (private val localRepocitory: LocalRepocitory) {
    suspend fun deleteAllProductUseCase() {
        localRepocitory.deleteAllProducts()
    }
}