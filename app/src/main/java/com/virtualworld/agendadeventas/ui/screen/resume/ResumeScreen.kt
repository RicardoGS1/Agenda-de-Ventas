package com.virtualworld.agendadeventas.ui.screen.resume

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState


@Composable
fun ResumeScreen(viewModel: ViewModelResume = hiltViewModel()) {

    val resumeScreenState by viewModel.resumeDataState.collectAsState()
    val screenUiState: ScreenUiState by viewModel.screenUiState.collectAsState()



    Box(modifier = Modifier.fillMaxSize()) {

        if (screenUiState != ScreenUiState.LOADING) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            ) {
                SeleccionarPeriodos(viewModel)

                LazyColumn() {

                    item {
                        CardResumeStores(
                            title = stringResource(id = R.string.inicio_unidades_vendidas),
                            colum2 = stringResource(id = R.string.inicio_info_unidades),
                            resumeScreenState.map { it.store },
                            resumeScreenState.map { it.units.toString() })
                    }

                    item {
                        CardResumeStores(
                            title = stringResource(id = R.string.inicio_ganancia),
                            colum2 = stringResource(id = R.string.inicio_info_ganancia),
                            resumeScreenState.map { it.store },
                            resumeScreenState.map { it.profit.toString() }
                        )
                    }
                }
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

        }
    }
}


@Composable
fun SeleccionarPeriodos(viewModel: ViewModelResume) {


    val dateEnd: Long? by viewModel.dateEnd.collectAsState(null)
    val dateStart: Long? by viewModel.dateStart.collectAsState(null)


    //CONTENEDOR PRINCIPAL
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MaterialTheme.colorScheme.primary),
    ) {

        Column {
            //TITULO
            Text(
                text = stringResource(id = R.string.inicio_priodo), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primaryContainer,
                textAlign = TextAlign.Center

            )

            //CONTENEDOR BOTONES
            Box(modifier = Modifier.fillMaxWidth()) {

                ButtonSelctFechaView(
                    Modifier.align(Alignment.CenterStart),
                    dateStart,
                    stringResource(R.string.inicio_desde),
                    0,
                    0,
                    0
                ) {
                    viewModel.changerDateStart(
                        it
                    )
                }
                ButtonSelctFechaView(
                    Modifier.align(Alignment.CenterEnd),
                    dateEnd,
                    stringResource(R.string.inicio_hasta),
                    24,
                    59,
                    59
                ) {
                    viewModel.changerDateEnd(
                        it
                    )
                }
            }
        }
    }
}



