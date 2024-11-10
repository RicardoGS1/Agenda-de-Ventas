package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive
import com.virtualworld.agendadeventas.domain.models.StoresValues
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductForStore @Inject constructor(
    private val localRepocitory: LocalRepository,
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val getProductAllUseCase: GetProductAllUseCase
) {

    fun getProductForStore(idStore: Int): Flow<NetworkResponseState<List<ProductStoreCore>>> {

        return localRepocitory.getAllProductsStore(idStore).map { response ->

            when (response) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Error -> NetworkResponseState.Error(response.exception)
                is NetworkResponseState.Success -> {

                    NetworkResponseState.Success(response.result)
                }
            }
        }
    }

    fun getProductsWithActivesStores(): Flow<NetworkResponseState<List<ProductWithStoresActive>>> {

        return combine(
            getStoresActiveUseCase.getStoresActive(),
            getProductAllUseCase()
        ) { storesActiveResponse, productsResponse ->

            when {
                storesActiveResponse is NetworkResponseState.Success && productsResponse is NetworkResponseState.Success -> {

                    val productWithStoresActive = productsResponse.result.map { product ->

                        val lismap: List<StoresValues> = storesActiveResponse.result.map {

                            when(it.idStore){
                                1 ->{ StoresValues("1", it.nameStore , product.venta1.toString())}
                                2 ->{ StoresValues("2", it.nameStore , product.venta2.toString())}
                                3 ->{StoresValues("3", it.nameStore , product.venta3.toString())}
                               4 ->{StoresValues("4", it.nameStore , product.venta4.toString())}
                                5 ->{StoresValues("5", it.nameStore , product.venta5.toString())}
                                else -> {StoresValues("", "" , "")}
                            }
                        }

                        ProductWithStoresActive(
                            idProduct = product.id.toString(),
                            productName = product.nombre,
                            productCost = product.compra.toString(),
                            storesValues = lismap
                        )


                    }
                    println(productWithStoresActive)
                    NetworkResponseState.Success(productWithStoresActive)
                }


                storesActiveResponse is NetworkResponseState.Error -> {
                    NetworkResponseState.Error(storesActiveResponse.exception)
                }

                productsResponse is NetworkResponseState.Error -> {
                    NetworkResponseState.Error(productsResponse.exception)
                }

                else -> {
                    NetworkResponseState.Loading
                }
            }


        }
    }


}