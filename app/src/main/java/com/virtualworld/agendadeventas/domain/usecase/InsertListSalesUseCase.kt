package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.SoldRoom
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import javax.inject.Inject

class InsertListSalesUseCase @Inject constructor(private val localRepocitory: LocalRepository) {

    suspend fun insertListSales(listSales: List<SoldRoom>) {
        localRepocitory.insertListSales(listSales)
    }
}