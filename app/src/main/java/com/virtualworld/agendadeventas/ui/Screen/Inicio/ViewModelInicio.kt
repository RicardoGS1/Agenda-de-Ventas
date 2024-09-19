package com.virtualworld.agendadeventas.ui.Screen.Inicio

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.virtualworld.agendadeventas.core.source.local.ProductoLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.TiendasLocalDataSource
import com.virtualworld.agendadeventas.core.source.local.VendidoLocalDataSourse
import com.virtualworld.agendadeventas.domain.UseCase.GetResumenStore
import com.virtualworld.agendadeventas.common.NetworkResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ViewModelInicio @Inject constructor(
    private val getResumenStore: GetResumenStore,

    ) : ViewModel() {

    private val _inicioScreenState = MutableStateFlow(listOf(GetResumenStore.DatosVestaUI()) )
    val inicioScreenState: StateFlow<List<GetResumenStore.DatosVestaUI>> = _inicioScreenState

    private val _dateStart = MutableStateFlow<Long?>(null)
    val dateStart: StateFlow<Long?> = _dateStart

    private val _dateEnd = MutableStateFlow<Long?>(null)
    val dateEnd: StateFlow<Long?> = _dateEnd


    fun changerDateEnd(selectedTimeInMillis: Long) {
        _dateEnd.update {
            selectedTimeInMillis
        }
        getResumeSellStoreActive()
    }

    fun changerDateStart(selectedTimeInMillis: Long) {
        _dateStart.update {
            selectedTimeInMillis
        }
        getResumeSellStoreActive()
    }


    fun getResumeSellStoreActive(){
        getResumenStore.getResumeSellStoreActive(_dateStart.value,_dateEnd.value).onEach {state->

            when (state) {

                is NetworkResponseState.Loading -> println("Loading")
                is NetworkResponseState.Error -> println("Error")
                is NetworkResponseState.Success -> {

                    _inicioScreenState.update {
                        state.result
                    }

                }


                }


        }.launchIn(viewModelScope)

    }





}





