package com.virtualworld.agendadeventas.ui.screen.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

import com.virtualword3d.salesregister.CasoUso.FirebaseUseCase
import com.virtualworld.agendadeventas.core.entity.ScreenUiState
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.domain.UseCase.DeleteAllProductUseCase
import com.virtualworld.agendadeventas.domain.UseCase.DeleteAllSalesUseCase
import com.virtualworld.agendadeventas.domain.UseCase.GetProductStore
import com.virtualworld.agendadeventas.domain.UseCase.GetSoldAllUseCase
import com.virtualworld.agendadeventas.domain.UseCase.InsertListSalesUseCase
import com.virtualworld.agendadeventas.domain.UseCase.SetListProductUseCase
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

    private val _screenUiState = MutableStateFlow(ScreenUiState.NEUTRAL)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState


    init {
        _identification.update { Firebase.auth.currentUser?.email.toString() }
    }

    fun onChangerScreenState(screenUiState: ScreenUiState) {
        _screenUiState.value = screenUiState
    }


    fun authenticateUser(email: String, password: String, isNewUser: Boolean) {

        viewModelScope.launch {
            when (val result = firebaseUseCase.authenticateUser(email, password, isNewUser)) {
                is NetworkResponseState.Loading -> {
                    onChangerScreenState(ScreenUiState.LOADING)
                }

                is NetworkResponseState.Success -> {
                    _identification.update { Firebase.auth.currentUser?.email.toString() }
                    onChangerScreenState(ScreenUiState.OK)
                }

                is NetworkResponseState.Error -> {
                    onChangerScreenState(ScreenUiState.ERROR)
                    println("Error: ${result.exception.message}")
                }
            }
        }
    }


    fun closeSession() {
        viewModelScope.launch {
            when (val result = firebaseUseCase.closeSecion()) {

                is NetworkResponseState.Loading -> {

                    onChangerScreenState(ScreenUiState.LOADING)
                }

                is NetworkResponseState.Success -> {
                    _identification.update { "" }
                    onChangerScreenState(ScreenUiState.OK)
                }

                is NetworkResponseState.Error -> {
                    onChangerScreenState(ScreenUiState.ERROR)
                    println("Error: ${result.exception.message}")
                }
            }

        }
    }


    fun exportSales() {


        viewModelScope.launch {

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
                        onChangerScreenState(ScreenUiState.LOADING)
                    }

                    is NetworkResponseState.Success -> {




                        onChangerScreenState(ScreenUiState.OK)
                    }

                    is NetworkResponseState.Error -> {
                        onChangerScreenState(ScreenUiState.ERROR)
                        println("Error: ${result.exception.message}")
                    }
                }

            }


        }

    }

    fun importSales() {
        viewModelScope.launch {
            val soldRoomList =  firebaseUseCase.importSoldRoomListFromFirestore(_identification.value)

            val productRoomList =  firebaseUseCase.importProductRoomListFromFirestore(_identification.value)

            if (soldRoomList is NetworkResponseState.Success && productRoomList is NetworkResponseState.Success){

                println("eliminando bd")
                deleteAllSalesUseCase.deleteAllSalesUseCase()
                insertListSalesUseCase.insertListSales(soldRoomList.result)

                deleteAllProductUseCase.deleteAllProductUseCase()
                inserListProductUseCase.insertListProduct(productRoomList.result)



            }



        }

    }

}


/*

        viewModelScope.launch {
            when (val result = casoUsoFirebase.exportSoldRoomListToFirestore(soldRoomList, userId)) {
                is NetworkResponseState.Loading -> {
                    // Mostrar un indicador de carga (opcional)
                }
                is NetworkResponseState.Success -> {
                    // La exportación fue exitosa
                }
                is NetworkResponseState.Error -> {
                    // Manejar el error
                    println("Error: ${result.exception.message}")
                }
            }
        }


        viewModelScope.launch {
            var connection = false

            vendidoRepo.getAllVendidos { listSales ->


                casoUsoFirebase.exportSales(listSales, { message ->
                    onChangeLoading(message)
                }) {
                    connection = it
                }
            }

            delay(5000)
            if (!connection) {
                onChangeLoading(Mensajes.ERROR)
                Log.e("efecto", "incompleto")
                FirebaseFirestore.getInstance().terminate()
            }
        }
    }








        fun importSales() {
            viewModelScope.launch {
                casoUsoFirebase.importSales({ message ->
                    onChangeLoading(message)
                },

                    { listSales ->
                        salveSales(listSales)
                    })
            }
        }

        private fun salveSales(listSales: List<SoldRoom>) {
            vendidoRepo.cleanSales { response ->

                Log.e("efecto", response.toString())

                vendidoRepo.addProductoVendido(listSales)

            }
        }



    }

 */