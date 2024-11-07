package com.virtualworld.agendadeventas.domain.usecase


import com.virtualworld.agendadeventas.LogApplication
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.repository.LocalRepository
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
@ExperimentalCoroutinesApi
class GetProductForStoreTestIntegration {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

   // @Inject
   // lateinit var localRepository: LocalRepository

    //private lateinit var getProductStore: GetProductStore
  //  private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
       // hiltRule.inject()
       // Dispatchers.setMain(testDispatcher)
      //  getProductStore = GetProductStore(localRepository)
    }

    @Test
    fun hhhhh () = runTest {
        // Arrange
        val idStore = 1

        // Act
       // val resultFlow = getProductStore.getProductForStore(idStore)

        // Assert
        1==1
    }

    // Agrega más pruebas para otros IDs de tienda y escenarios de error
}