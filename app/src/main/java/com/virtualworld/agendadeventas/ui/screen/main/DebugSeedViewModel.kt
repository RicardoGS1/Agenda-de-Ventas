package com.virtualworld.agendadeventas.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.domain.usecase.SeedTestDataUseCase
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugSeedViewModel @Inject constructor(
    private val seedTestDataUseCase: SeedTestDataUseCase,
) : ViewModel() {

    private val _seedState = MutableStateFlow<ScreenUiState?>(null)
    val seedState: StateFlow<ScreenUiState?> = _seedState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun seedTestData() {
        if (_isLoading.value) return
        viewModelScope.launch {
            _isLoading.update { true }
            _seedState.update { ScreenUiState.LOADING }
            val result = seedTestDataUseCase()
            _seedState.update { result }
            _isLoading.update { false }
        }
    }

    fun clearSeedState() {
        _seedState.update { null }
    }
}
