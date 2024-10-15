package com.virtualworld.agendadeventas.domain.UseCase

import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepocitory
import javax.inject.Inject

class InsertListSalesUseCase @Inject constructor(private val localRepocitory: LocalRepocitory) {

    suspend fun insertListSales(listSales: List<SoldRoom>) {
        localRepocitory.insertListSales(listSales)
    }
}