package com.virtualworld.agendadeventas.domain.usecase

import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.ui.screen.add.ProductUiState
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val localRepository: LocalRepository)  {

    suspend operator fun invoke(productUiState: ProductUiState): ScreenUiState {

        val productRoom = ProductRoom(
            nombre = productUiState.productName,
            compra = productUiState.productCost.ifEmpty { "0" }.toLong(),
            venta1 = productUiState.storeValues[1]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta2 = productUiState.storeValues[2]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta3 = productUiState.storeValues[3]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta4 = productUiState.storeValues[4]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta5 = productUiState.storeValues[5]?.ifEmpty { "0" }?.toLong() ?: 0
        )

        val response = localRepository.addProduct(productRoom)

        return when(response){
            is NetworkResponseState.Error -> ScreenUiState.ERROR
            is NetworkResponseState.Loading -> ScreenUiState.LOADING
            is NetworkResponseState.Success -> ScreenUiState.OK
        }


    }


}