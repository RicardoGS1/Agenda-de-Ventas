package com.virtualworld.agendadeventas.ui.screen.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState

import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.Model.SoldForStore
import com.virtualworld.agendadeventas.core.source.local.VendidoLocalDataSourse
import com.virtualworld.agendadeventas.domain.UseCase.GetProductStore
import com.virtualworld.agendadeventas.domain.UseCase.GetSoldForStoreUseCase
import com.virtualworld.agendadeventas.domain.UseCase.GetStoresActiveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val getProductStore: GetProductStore,
    private val getSoldForStoreUseCase: GetSoldForStoreUseCase,
    private val vendidoRepo: VendidoLocalDataSourse
) : ViewModel() {

    private val _storesActiveState = MutableStateFlow(listOf(Pair(-1, "")))
    val storesActiveState: StateFlow<List<Pair<Int, String>>> = _storesActiveState


    private val _soldForStore = MutableStateFlow(listOf(SoldForStore()))
    val soldForStore: StateFlow<List<SoldForStore>> = _soldForStore


    private val _messengerState = MutableStateFlow(ScreenUiState.LOADING)
    val messengerState: StateFlow<ScreenUiState> = _messengerState


    init {

        getStoresActive()
    }

    fun getSoldForStore(idStore: Int){
        getSoldForStoreUseCase.getSoldForStore(idStore).onEach { state->


            when(state){
                is NetworkResponseState.Error -> println("")
                is NetworkResponseState.Loading -> println("")
                is NetworkResponseState.Success -> _soldForStore.update { state.result }
            }
        }.launchIn(viewModelScope)


    }

    fun getStoresActive() {

        getStoresActiveUseCase.GetTiendasActivas().onEach { state ->

            when (state) {
                is NetworkResponseState.Error -> _messengerState.update { ScreenUiState.ERROR }
                is NetworkResponseState.Loading -> ScreenUiState.LOADING
                is NetworkResponseState.Success -> {

                    _storesActiveState.update {
                        state.result.map { Pair(it.idStore, it.nameStore) }
                    }

                    _messengerState.update {
                        ScreenUiState.NEUTRAL
                    }
                }
            }
        }.launchIn(viewModelScope)

    }


}