package com.virtualworld.agendadeventas.ui.screen.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

import com.virtualword3d.salesregister.CasoUso.FirebaseUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.domain.usecase.DeleteAllProductUseCase
import com.virtualworld.agendadeventas.domain.usecase.DeleteAllSalesUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetProductStore
import com.virtualworld.agendadeventas.domain.usecase.GetSoldAllUseCase
import com.virtualworld.agendadeventas.domain.usecase.InsertListSalesUseCase
import com.virtualworld.agendadeventas.domain.usecase.SetListProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val getSoldAllUseCase: GetSoldAllUseCase,
    private val firebaseUseCase: FirebaseUseCase,
    private val deleteAllSalesUseCase: DeleteAllSalesUseCase,
    private val insertListSalesUseCase: InsertListSalesUseCase,
    private val getProductStore: GetProductStore,
    private val deleteAllProductUseCase: DeleteAllProductUseCase,
    private val inserListProductUseCase: SetListProductUseCase
) : ViewModel() {

    private val _identification = MutableStateFlow("")
    val identification: StateFlow<String> = _identification

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState


    init {
        _identification.update { Firebase.auth.currentUser?.email.toString() }
        changerUiState(ScreenUiState.NEUTRAL)
    }

    fun changerUiState(message: ScreenUiState) {
        _screenUiState.update {
            message
        }
    }


    fun authenticateUser(email: String, password: String, isNewUser: Boolean) {

        viewModelScope.launch {
            when (val result = firebaseUseCase.authenticateUser(email, password, isNewUser)) {
                is NetworkResponseState.Loading -> {
                    changerUiState(ScreenUiState.LOADING)
                }

                is NetworkResponseState.Success -> {
                    _identification.update { Firebase.auth.currentUser?.email.toString() }
                    changerUiState(ScreenUiState.OK)
                }

                is NetworkResponseState.Error -> {
                    changerUiState(ScreenUiState.ERROR)
                    println("Error: ${result.exception.message}")
                }
            }
        }
    }


    fun closeSession() {
        viewModelScope.launch {
            when (val result = firebaseUseCase.closeSecion()) {

                is NetworkResponseState.Loading -> {

                    changerUiState(ScreenUiState.LOADING)
                }

                is NetworkResponseState.Success -> {
                    _identification.update { "" }
                    changerUiState(ScreenUiState.OK)
                }

                is NetworkResponseState.Error -> {
                    changerUiState(ScreenUiState.ERROR)
                    println("Error: ${result.exception.message}")
                }
            }

        }
    }


    fun exportSales() {


        viewModelScope.launch {

            changerUiState(ScreenUiState.LOADING)

            val soldRoomList = getSoldAllUseCase.getSoldAll().filter {
                it is NetworkResponseState.Success
            }.first()

            val listProduct = getProductStore.getAllProductRoom().filter {
                it is NetworkResponseState.Success
            }.first()

            if (soldRoomList is NetworkResponseState.Success && listProduct is NetworkResponseState.Success) {

                when (val result = firebaseUseCase.exportSoldRoomListToFirestore(
                    soldRoomList.result,
                    listProduct.result,
                    _identification.value
                )) {
                    is NetworkResponseState.Loading -> {
                        changerUiState(ScreenUiState.LOADING)
                    }

                    is NetworkResponseState.Success -> {

                        changerUiState(ScreenUiState.OK)

                    }

                    is NetworkResponseState.Error -> {
                        changerUiState(ScreenUiState.ERROR)
                        println("Error: ${result.exception.message}")
                    }
                }

            }


        }

    }

    fun importSales() {
        viewModelScope.launch {

            changerUiState(ScreenUiState.LOADING)


            val productRoomList =
                firebaseUseCase.importProductRoomListFromFirestore(_identification.value)

            val soldRoomList =
                firebaseUseCase.importSoldRoomListFromFirestore(_identification.value)




            if (soldRoomList is NetworkResponseState.Success && productRoomList is NetworkResponseState.Success) {

                println("eliminando bd")
                deleteAllSalesUseCase.deleteAllSalesUseCase()
                insertListSalesUseCase.insertListSales(soldRoomList.result)

                deleteAllProductUseCase.deleteAllProductUseCase()
                inserListProductUseCase.insertListProduct(productRoomList.result)

                changerUiState(ScreenUiState.OK)


            }

            if (soldRoomList is NetworkResponseState.Error || productRoomList is NetworkResponseState.Error){
                changerUiState(ScreenUiState.ERROR)
            }


        }

    }

}

