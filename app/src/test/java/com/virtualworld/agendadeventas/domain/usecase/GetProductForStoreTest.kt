package com.virtualworld.agendadeventas.domain.usecase


import app.cash.turbine.test
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.ProductStoreCore
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class GetProductForStoreTest {


    private val localRepository: LocalRepository = mockk() // Creamos un mock del repositorio
    private val getStoresActiveUseCase: GetStoresActiveUseCase = mockk()
    private val getProductAllUseCase: GetProductAllUseCase  = mockk()

    private lateinit var getProductForStore: GetProductForStore // Instancia de la clase que vamos a probar

    @Before
    fun onBefore() {
        //MockKAnnotations.init(this)
        getProductForStore = GetProductForStore(localRepository,getStoresActiveUseCase, getProductAllUseCase) // Inicializamos la clase con el mock
    }


    @Test
    fun `getProductForStore debe devolver Success cuando el repositorio devuelve Success`() =
        runTest {

            // Arrange (Preparar)
            val idStore = 1
            val products =
                listOf(ProductStoreCore(1, "Producto 1", 10.0.toLong(), 9)) // Datos de ejemplo

            coEvery { localRepository.getAllProductsStore(idStore) } returns flow {
                emit(NetworkResponseState.Loading) // Emitir Loading primero
                emit(NetworkResponseState.Success(products)) // Luego emitir Success
                // Mockeamos el repositorio para que devuelva Success
            }

            // Act (Actuar)
            val resultFlow =
                getProductForStore.getProductForStore(idStore) // Llamamos a la función que estamos probando

            // Assert (Verificar)
            resultFlow.test {

                assertEquals(
                    NetworkResponseState.Loading,
                    awaitItem()
                ) // Verificamos que se emite Loading primero

                assertEquals(
                    NetworkResponseState.Success(products),
                    awaitItem()
                ) // Verificamos que se emite Success con los datos esperados

                awaitComplete() // Verificamos que el flujo se completa}
            }

        }


    @Test
    fun `getProductForStore debe devolver Error cuando el repositorio devuelve Error`() =

        runTest {

            // Arrange
            val idStore = 1
            val exception = Exception("Excepción de prueba")

            coEvery { localRepository.getAllProductsStore(idStore) } returns flowOf(
                NetworkResponseState.Error(
                    exception
                )
            )
            // Mockeamos el repositorio para que devuelva Error

            // Act
            val resultFlow = getProductForStore.getProductForStore(idStore)

            // Assert
            resultFlow.test {
                // assertEquals(NetworkResponseState.Loading, awaitItem())

                assertEquals(
                    NetworkResponseState.Error(exception),
                    awaitItem()
                ) // Verificamos que se emite Error con la excepción esperada
                awaitComplete()
            }
        }


}