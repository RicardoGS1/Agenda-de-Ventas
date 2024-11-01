package com.virtualworld.agendadeventas.ui.screen.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

import com.virtualworld.agendadeventas.domain.usecase.AuthenticateUserUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.domain.usecase.ExportAllDataUseCase
import com.virtualworld.agendadeventas.domain.usecase.ImportAllDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val exportAllDataUseCase: ExportAllDataUseCase,
    private val importAllDataUseCase: ImportAllDataUseCase,
    private val authenticateUserUseCase: AuthenticateUserUseCase,

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
            when (val result = authenticateUserUseCase.authenticateUser(email, password, isNewUser)) {
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
            when (val result = authenticateUserUseCase.closeSecion()) {

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


    fun exportData() {
        viewModelScope.launch {
            changerUiState(ScreenUiState.LOADING)
            val response = exportAllDataUseCase(_identification.value)
            changerUiState(response)
        }
    }

    fun importSales() {
        viewModelScope.launch {

            changerUiState(ScreenUiState.LOADING)
            val response = importAllDataUseCase(_identification.value)
            changerUiState(response)


    }
    }

}

