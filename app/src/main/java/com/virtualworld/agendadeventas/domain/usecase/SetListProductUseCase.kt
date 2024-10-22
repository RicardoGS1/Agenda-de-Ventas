package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import javax.inject.Inject

class SetListProductUseCase @Inject constructor(private val localRepocitory: LocalRepository) {
    suspend fun insertListProduct(result: List<ProductRoom>) {

        localRepocitory.insertListProducts(result)
    }


}