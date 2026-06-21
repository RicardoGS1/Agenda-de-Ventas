package com.virtualworld.agendadeventas.ui.screen.ai

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualworld.agendadeventas.core.locale.LocaleManager
import com.virtualworld.agendadeventas.domain.model.AiAnalysisType
import com.virtualworld.agendadeventas.domain.usecase.RunAiAnalysisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AiAnalysisResultState(
    val type: AiAnalysisType? = null,
    val resultText: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class AiAnalysisViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val runAiAnalysisUseCase: RunAiAnalysisUseCase,
) : ViewModel() {

    private val _resultState = MutableStateFlow(AiAnalysisResultState())
    val resultState: StateFlow<AiAnalysisResultState> = _resultState.asStateFlow()

    val analysisTypes: List<AiAnalysisType> = AiAnalysisType.entries

    fun runAnalysis(type: AiAnalysisType) {
        if (_resultState.value.isLoading) return

        viewModelScope.launch {
            _resultState.update {
                AiAnalysisResultState(type = type, isLoading = true)
            }

            val language = LocaleManager.getSelectedLanguage(context)
            val result = runAiAnalysisUseCase(type, language)

            _resultState.update { current ->
                result.fold(
                    onSuccess = { text ->
                        current.copy(
                            type = type,
                            resultText = text,
                            isLoading = false,
                            errorMessage = null,
                        )
                    },
                    onFailure = { error ->
                        current.copy(
                            type = type,
                            resultText = null,
                            isLoading = false,
                            errorMessage = error.message ?: "Unknown error",
                        )
                    },
                )
            }
        }
    }

    fun clearResult() {
        _resultState.update { AiAnalysisResultState() }
    }

    fun dismissError() {
        _resultState.update { it.copy(errorMessage = null, isLoading = false) }
    }
}
