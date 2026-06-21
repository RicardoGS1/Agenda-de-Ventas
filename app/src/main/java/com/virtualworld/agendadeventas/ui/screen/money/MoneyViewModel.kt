package com.virtualworld.agendadeventas.ui.screen.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.core.model.CashOperationCore
import com.virtualworld.agendadeventas.core.model.StoreCashSummaryCore
import com.virtualworld.agendadeventas.core.repository.InsufficientCashException
import com.virtualworld.agendadeventas.domain.usecase.AddCashToStoreUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoreCashOperationsUseCase
import com.virtualworld.agendadeventas.domain.usecase.GetStoreCashSummariesUseCase
import com.virtualworld.agendadeventas.domain.usecase.WithdrawCashFromStoreUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CashDialogMode {
    NONE,
    DEPOSIT,
    WITHDRAW,
}

@HiltViewModel
class MoneyViewModel @Inject constructor(
    private val getStoreCashSummariesUseCase: GetStoreCashSummariesUseCase,
    private val getStoreCashOperationsUseCase: GetStoreCashOperationsUseCase,
    private val addCashToStoreUseCase: AddCashToStoreUseCase,
    private val withdrawCashFromStoreUseCase: WithdrawCashFromStoreUseCase,
) : ViewModel() {

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    private val _storeSummaries = MutableStateFlow<List<StoreCashSummaryCore>>(emptyList())
    val storeSummaries: StateFlow<List<StoreCashSummaryCore>> = _storeSummaries

    private val _operations = MutableStateFlow<List<CashOperationCore>>(emptyList())
    val operations: StateFlow<List<CashOperationCore>> = _operations

    private val _selectedStoreId = MutableStateFlow<Int?>(null)
    val selectedStoreId: StateFlow<Int?> = _selectedStoreId

    private val _dialogMode = MutableStateFlow(CashDialogMode.NONE)
    val dialogMode: StateFlow<CashDialogMode> = _dialogMode

    private val _insufficientCashError = MutableStateFlow(false)
    val insufficientCashError: StateFlow<Boolean> = _insufficientCashError

    private var operationsJob: Job? = null

    init {
        observeSummaries()
    }

    private fun observeSummaries() {
        getStoreCashSummariesUseCase().onEach { state ->
            when (state) {
                is NetworkResponseState.Error -> _screenUiState.update { ScreenUiState.ERROR }
                is NetworkResponseState.Loading -> Unit
                is NetworkResponseState.Success -> {
                    _storeSummaries.update { state.result }
                    _screenUiState.update { ScreenUiState.NEUTRAL }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun selectStore(storeId: Int?) {
        _selectedStoreId.value = storeId
        operationsJob?.cancel()
        if (storeId == null) {
            _operations.update { emptyList() }
            return
        }
        operationsJob = getStoreCashOperationsUseCase(storeId).onEach { state ->
            when (state) {
                is NetworkResponseState.Error -> Unit
                is NetworkResponseState.Loading -> Unit
                is NetworkResponseState.Success -> _operations.update { state.result }
            }
        }.launchIn(viewModelScope)
    }

    fun openDepositDialog() {
        _insufficientCashError.update { false }
        _dialogMode.update { CashDialogMode.DEPOSIT }
    }

    fun openWithdrawDialog() {
        _insufficientCashError.update { false }
        _dialogMode.update { CashDialogMode.WITHDRAW }
    }

    fun dismissDialog() {
        _dialogMode.update { CashDialogMode.NONE }
        _insufficientCashError.update { false }
    }

    fun confirmCashOperation(amountText: String, note: String) {
        val storeId = _selectedStoreId.value ?: return
        val amount = amountText.replace(",", ".").toFloatOrNull()
        if (amount == null || amount <= 0f) {
            _screenUiState.update { ScreenUiState.ERROR }
            return
        }

        viewModelScope.launch {
            _screenUiState.update { ScreenUiState.LOADING }
            val result = when (_dialogMode.value) {
                CashDialogMode.DEPOSIT -> addCashToStoreUseCase(storeId, amount, note)
                CashDialogMode.WITHDRAW -> withdrawCashFromStoreUseCase(storeId, amount, note)
                CashDialogMode.NONE -> return@launch
            }
            when (result) {
                is NetworkResponseState.Success -> {
                    dismissDialog()
                    _screenUiState.update { ScreenUiState.OK }
                }
                is NetworkResponseState.Error -> {
                    if (result.exception is InsufficientCashException) {
                        _insufficientCashError.update { true }
                        _screenUiState.update { ScreenUiState.NEUTRAL }
                    } else {
                        _screenUiState.update { ScreenUiState.ERROR }
                    }
                }
                is NetworkResponseState.Loading -> Unit
            }
        }
    }

    fun changerUiState(state: ScreenUiState) {
        _screenUiState.update { state }
    }
}
