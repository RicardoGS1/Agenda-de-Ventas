package com.virtualworld.agendadeventas.domain.UseCase

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepocitory
import javax.inject.Inject

class SetListProductUseCase @Inject constructor(private val localRepocitory: LocalRepocitory) {
    suspend fun insertListProduct(result: List<ProductRoom>) {

        localRepocitory.insertListProducts(result)
    }


}