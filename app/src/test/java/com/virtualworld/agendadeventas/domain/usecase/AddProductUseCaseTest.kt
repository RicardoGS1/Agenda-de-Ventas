package com.virtualworld.agendadeventas.domain.usecase

import app.cash.turbine.test
import com.virtualword3d.salesregister.Data.Entity.ProductRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.ProductStoreCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import com.virtualworld.agendadeventas.ui.screen.add.ProductUiState
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddProductUseCaseTest{


    private val localRepository = mockk<LocalRepository>()
    private val addProductUseCase = AddProductUseCase(localRepository)


    @Test
    fun `invoke debería retornar ScreenUiState_OK aun cuando se envian datos vasios y nulos y convertitlos a 0 `() = runTest {

        // Arrange (Preparar)
        val productUiState = ProductUiState(
            productName = "Nombre del producto",
            productCost = "10",
            storeValues = mapOf(1 to "15", 2 to "20", 5 to "")
        )

        val productRoom = ProductRoom(
            nombre = productUiState.productName,
            compra = productUiState.productCost.ifEmpty { "0" }.toLong(),
            venta1 = productUiState.storeValues[1]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta2 = productUiState.storeValues[2]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta3 = productUiState.storeValues[3]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta4 = productUiState.storeValues[4]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta5 = productUiState.storeValues[5]?.ifEmpty { "0" }?.toLong() ?: 0,
        )

        val result1 =  ProductRoom(nombre="Nombre del producto", compra = 10L, venta1 = 15L, venta2 = 20L, venta3 = 0L, venta4 = 0L, venta5 = 0L)

        coEvery { localRepository.addProduct(productRoom) } returns NetworkResponseState.Success(Unit)

        // Act (Actuar)
        val result = addProductUseCase(productUiState)

        // Assert (Verificar)
        assertEquals(ScreenUiState.OK, result)
        assertEquals( result1, productRoom )

    }

    @Test
    fun `invoke debería retornar ScreenUiState_OK cuando el repositorio retorna Success`() = runTest {

        // Arrange (Preparar)
        val productUiState = ProductUiState(
            productName = "Nombre del producto",
            productCost = "10",
            storeValues = mapOf(1 to "15", 2 to "20")
        )

        val productRoom = ProductRoom(
            nombre = productUiState.productName,
            compra = productUiState.productCost.ifEmpty { "0" }.toLong(),
            venta1 = productUiState.storeValues[1]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta2 = productUiState.storeValues[2]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta3 = productUiState.storeValues[3]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta4 = productUiState.storeValues[4]?.ifEmpty { "0" }?.toLong() ?: 0,
            venta5 = productUiState.storeValues[5]?.ifEmpty { "0" }?.toLong() ?: 0,
        )

        coEvery { localRepository.addProduct(productRoom) } returns NetworkResponseState.Success(Unit)

        // Act (Actuar)
        val result = addProductUseCase(productUiState)

        // Assert (Verificar)
        assertEquals(ScreenUiState.OK, result)
    }

    @Test
    fun `invoke debería retornar ScreenUiState_ERROR cuando el repositorio retorna Error`() = runTest {
        // Arrange (Preparar)
        val productUiState = ProductUiState(
            productName = "Nombre del producto",
            productCost = "10",
            storeValues = mapOf(1 to "15", 2 to "20")
        )
        coEvery { localRepository.addProduct(any()) } returns NetworkResponseState.Error(Exception("Excepción de prueba"))

        // Act (Actuar)
        val result = addProductUseCase(productUiState)

        // Assert (Verificar)
        assertEquals(ScreenUiState.ERROR, result)
    }

    @Test
    fun `invoke debería retornar ScreenUiState_LOADING cuando el repositorio retorna Loading`() = runTest {
        // Arrange (Preparar)
        val productUiState = ProductUiState(
            productName = "Nombre del producto",
            productCost = "10",
            storeValues = mapOf(1 to "15", 2 to "20")
        )

        coEvery { localRepository.addProduct(any()) } returns NetworkResponseState.Loading

        // Act (Actuar)
        val result = addProductUseCase(productUiState)

        // Assert (Verificar)
        assertEquals(ScreenUiState.LOADING, result)
    }
}