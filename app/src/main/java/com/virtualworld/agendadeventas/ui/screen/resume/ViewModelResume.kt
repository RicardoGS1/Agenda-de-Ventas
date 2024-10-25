package com.virtualworld.agendadeventas.ui.screen.resume

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.virtualworld.agendadeventas.domain.usecase.GetResumeStoresUseCase
import com.virtualworld.agendadeventas.common.NetworkResponseState
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelResume @Inject constructor(
    private val getResumeStoresUseCase: GetResumeStoresUseCase,
) : ViewModel() {

    private val _screenUiState = MutableStateFlow(ScreenUiState.LOADING)
    val screenUiState: StateFlow<ScreenUiState> = _screenUiState

    private val _resumeDataState = MutableStateFlow(listOf(ResumeDataUI()))
    val resumeDataState: StateFlow<List<ResumeDataUI>> = _resumeDataState

    private val _dateStart = MutableStateFlow<Long?>(null)
    val dateStart: StateFlow<Long?> = _dateStart

    private val _dateEnd = MutableStateFlow<Long?>(null)
    val dateEnd: StateFlow<Long?> = _dateEnd

    init {
        getResumeSoldStoreActive()
    }

    fun changerUiState(message: ScreenUiState) {
        _screenUiState.update {
            message
        }
    }

    fun changerDateEnd(selectedTimeInMillis: Long) {
        _dateEnd.update {
            selectedTimeInMillis
        }
        getResumeSoldStoreActive()
    }

    fun changerDateStart(selectedTimeInMillis: Long) {
        _dateStart.update {
            selectedTimeInMillis
        }
        getResumeSoldStoreActive()
    }


    private fun getResumeSoldStoreActive() {

        viewModelScope.launch {
            getResumeStoresUseCase.getResumeSellStoreActive(_dateStart.value, _dateEnd.value)
                .collect() { state ->

                    when (state) {

                        is NetworkResponseState.Loading -> changerUiState(ScreenUiState.LOADING)
                        is NetworkResponseState.Error -> changerUiState(ScreenUiState.ERROR)
                        is NetworkResponseState.Success -> {

                            _resumeDataState.update {
                                state.result
                            }
                            changerUiState(ScreenUiState.NEUTRAL)
                        }
                    }
                }
        }

    }


}





