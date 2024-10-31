package com.virtualworld.agendadeventas.ui.screen.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualword3d.salesregister.Data.Entity.StoreRoom
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.domain.usecase.GetAllStoreUseCase
import com.virtualworld.agendadeventas.domain.usecase.UpdateStoresUseCase

import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelTienda @Inject constructor(
    private val getAllStoreUseCase: GetAllStoreUseCase,
    private val updateStoresUseCase: UpdateStoresUseCase
) :
    ViewModel() {

    private val _storesAllState = MutableStateFlow<List<StoreRoom>>(emptyList())
    val storesAllState: StateFlow<List<StoreRoom>> = _storesAllState

    private val _screenUiState = MutableStateFlow(ScreenUiState.NEUTRAL)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    init {
        getAllStores()
    }

    fun changerUiState(message: ScreenUiState) {
        _screenUiState.update {

            message
        }
    }

    private fun getAllStores() {

        viewModelScope.launch {

            getAllStoreUseCase.getAllStores().collect { state ->

                when (state) {
                    is NetworkResponseState.Error -> _screenUiState.update { ScreenUiState.ERROR }
                    is NetworkResponseState.Loading -> ScreenUiState.LOADING
                    is NetworkResponseState.Success -> {


                        _storesAllState.update {
                            state.result
                        }

                        //para evitar que se sobreponga a otra actualizacion en proceso
                        if(_screenUiState.value!=ScreenUiState.OK)
                            changerUiState(ScreenUiState.NEUTRAL)


                    }
                }
            }

        }
    }

    fun onChangeNameStore(nameStore: String, idStore: Int) {

        _storesAllState.update { listStoreRoom ->

            listStoreRoom.map {

                if (it.id.toInt() == idStore) {
                    StoreRoom(it.id, nameStore, it.activa)
                } else {
                    it
                }

            }

        }
    }

    fun onChangeActiveStore(active: Boolean, idStore: Int) {

        _storesAllState.update { listStoreRoom ->

            listStoreRoom.map {

                if (it.id.toInt() == idStore) {
                    StoreRoom(it.id, it.nombre, active)
                } else {
                    it
                }

            }

        }
    }


    fun updateStore() {
        viewModelScope.launch {
            changerUiState(ScreenUiState.LOADING)
            updateStoresUseCase(_storesAllState.value)

            changerUiState(ScreenUiState.OK)
        }

    }
}




