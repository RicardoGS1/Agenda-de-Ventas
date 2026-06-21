package com.virtualworld.agendadeventas.ui.screen.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.PurchaseForStoreCore
import com.virtualworld.agendadeventas.domain.usecase.GetPurchasesForStoreUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoresActiveUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PurchaseRecordViewModel @Inject constructor(
    private val getStoresActiveUseCase: GetStoresActiveUseCase,
    private val getPurchasesForStoreUseCase: GetPurchasesForStoreUseCase,
) : ViewModel() {

    private val _storesActiveState = MutableStateFlow(listOf(Pair(-1, "")))
    val storesActiveState: StateFlow<List<Pair<Int, String>>> = _storesActiveState

    private val _purchasesForStore = MutableStateFlow(listOf(PurchaseForStoreCore()))
    val purchasesForStore: StateFlow<List<PurchaseForStoreCore>> = _purchasesForStore

    private val _messengerState = MutableStateFlow(ScreenUiState.LOADING)
    val messengerState: StateFlow<ScreenUiState> = _messengerState

    init {
        getStoresActive()
    }

    fun getPurchasesForStore(idStore: Int) {
        getPurchasesForStoreUseCase.getPurchasesForStore(idStore).onEach { state ->
            when (state) {
                is NetworkResponseState.Error -> _messengerState.update { ScreenUiState.ERROR }
                is NetworkResponseState.Loading -> _messengerState.update { ScreenUiState.LOADING }
                is NetworkResponseState.Success -> {
                    _purchasesForStore.update { state.result }
                    _messengerState.update { ScreenUiState.NEUTRAL }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getStoresActive() {
        getStoresActiveUseCase.getStoresActive().onEach { state ->
            when (state) {
                is NetworkResponseState.Error -> _messengerState.update { ScreenUiState.ERROR }
                is NetworkResponseState.Loading -> _messengerState.update { ScreenUiState.LOADING }
                is NetworkResponseState.Success -> {
                    _storesActiveState.update {
                        state.result.map { Pair(it.idStore, it.nameStore) }
                    }
                    _messengerState.update { ScreenUiState.NEUTRAL }
                }
            }
        }.launchIn(viewModelScope)
    }
}
