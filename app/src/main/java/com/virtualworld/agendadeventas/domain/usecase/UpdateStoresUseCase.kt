package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import javax.inject.Inject

class UpdateStoresUseCase @Inject constructor(private val localRepository: LocalRepository) {


    suspend operator fun invoke(storeRoom: List<StoreRoom>){
        localRepository.updateStores(storeRoom)

    }

}