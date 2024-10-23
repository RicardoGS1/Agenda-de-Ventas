package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.Repocitory.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductStore @Inject constructor(private val localRepocitory: LocalRepository) {

    fun getProductForStore(idStore:Int):Flow<NetworkResponseState<List<ProductStoreCore>>>{

        return localRepocitory.getAllProductsStore(idStore).map { response->

            when(response){
                is NetworkResponseState.Loading->NetworkResponseState.Loading
                is NetworkResponseState.Error->NetworkResponseState.Error(response.exception)
                is NetworkResponseState.Success-> {

                    NetworkResponseState.Success(response.result)
                }
            }



        }

    }




}