package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.repository.LocalRepository
import javax.inject.Inject

class DeleteProductUseCase  @Inject constructor(private val localRepocitory: LocalRepository) {

    suspend operator fun invoke(id:Int){

        localRepocitory.deleteProduct(id)

    }

}