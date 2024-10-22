package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import javax.inject.Inject

class DeleteAllSalesUseCase @Inject constructor (private val localRepocitory: LocalRepository)
{

    suspend fun deleteAllSalesUseCase(){
        localRepocitory.deleteAllSales()
    }


}