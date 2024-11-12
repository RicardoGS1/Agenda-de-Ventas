package com.virtualworld.agendadeventas.domain.usecase

import com.virtualworld.agendadeventas.core.entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.ui.screen.common.ProductUiState
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val localRepository: LocalRepository)  {

    suspend operator fun invoke(productUiState: ProductUiState): ScreenUiState {

        val productRoom = ProductRoom(
            nombre = productUiState.productName,
            compra = productUiState.productCost.ifEmpty { "0" }.toFloat() ?: 0f,
            venta1 = productUiState.storeValues[1]?.ifEmpty { "0" }?.toFloat() ?: 0f,
            venta2 = productUiState.storeValues[2]?.ifEmpty { "0" }?.toFloat() ?: 0f,
            venta3 = productUiState.storeValues[3]?.ifEmpty { "0" }?.toFloat() ?: 0f,
            venta4 = productUiState.storeValues[4]?.ifEmpty { "0" }?.toFloat() ?: 0f,
            venta5 = productUiState.storeValues[5]?.ifEmpty { "0" }?.toFloat() ?: 0f,
        )

        println(productRoom)

        val response = localRepository.addProduct(productRoom)

        return when(response){
            is NetworkResponseState.Error -> ScreenUiState.ERROR
            is NetworkResponseState.Loading -> ScreenUiState.LOADING
            is NetworkResponseState.Success -> ScreenUiState.OK
        }
    }

    suspend fun updateProduct(productUiState: ProductRoom): NetworkResponseState<Unit> {

        val response = localRepository.addProduct(productUiState)

        return when(response){
            is NetworkResponseState.Error -> NetworkResponseState.Error(response.exception)
            is NetworkResponseState.Loading -> NetworkResponseState.Loading
            is NetworkResponseState.Success -> NetworkResponseState.Success(response.result)
        }


    }


}