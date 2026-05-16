package com.virtualworld.agendadeventas.ui.screen.resume

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun ResumeScreen(
    viewModel: ViewModelResume = hiltViewModel(),
    onNavigateToStores: () -> Unit = {},
) {

    val resumeScreenState by viewModel.resumeDataState.collectAsState()
    val screenUiState: ScreenUiState by viewModel.screenUiState.collectAsState()
    val hasProducts by viewModel.hasProducts.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        if (screenUiState != ScreenUiState.LOADING) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            ) {
                SelectPeriod(viewModel)

                if (hasProducts) {
                    LazyColumn {
                        item {
                            CardResumeStores(
                                title = stringResource(id = R.string.inicio_unidades_vendidas),
                                colum2 = stringResource(id = R.string.inicio_info_unidades),
                                detalleCol1 = resumeScreenState.map { it.store },
                                detalleCol2 = resumeScreenState.map { it.units.toString() },
                                infoDescription = stringResource(id = R.string.inicio_desc_unidades),
                            )
                        }

                        item {
                            CardResumeStores(
                                title = stringResource(id = R.string.inicio_importe_total_venta),
                                colum2 = stringResource(id = R.string.inicio_info_importe),
                                detalleCol1 = resumeScreenState.map { it.store },
                                detalleCol2 = resumeScreenState.map { it.totalSales },
                                infoDescription = stringResource(id = R.string.inicio_desc_importe),
                            )
                        }

                        item {
                            CardResumeStores(
                                title = stringResource(id = R.string.inicio_inversion_recuperada),
                                colum2 = stringResource(id = R.string.inicio_info_inversion),
                                detalleCol1 = resumeScreenState.map { it.store },
                                detalleCol2 = resumeScreenState.map { it.recoveredInvestment },
                                infoDescription = stringResource(id = R.string.inicio_desc_inversion),
                            )
                        }

                        item {
                            CardResumeStores(
                                title = stringResource(id = R.string.inicio_ganancia),
                                colum2 = stringResource(id = R.string.inicio_info_ganancia),
                                detalleCol1 = resumeScreenState.map { it.store },
                                detalleCol2 = resumeScreenState.map { it.profit },
                                infoDescription = stringResource(id = R.string.inicio_desc_ganancia),
                            )
                        }
                    }
                } else {
                    ResumeEmptyState(onNavigateToStores = onNavigateToStores)
                }
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

        }
    }
}

@Composable
private fun ResumeEmptyState(onNavigateToStores: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.inicio_empty_message),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigateToStores,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(text = stringResource(id = R.string.inicio_empty_button))
        }
    }
}


@Composable
fun SelectPeriod(viewModel: ViewModelResume) {


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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ButtonSelctFechaView(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.inicio_fecha_inicio),
                    stateFecha = dateStart,
                    placeholder = stringResource(R.string.inicio_desde),
                    h = 0,
                    m = 0,
                    s = 0,
                    onDateSelected = { viewModel.changerDateStart(it) },
                )
                ButtonSelctFechaView(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.inicio_fecha_final),
                    stateFecha = dateEnd,
                    placeholder = stringResource(R.string.inicio_hasta),
                    h = 23,
                    m = 59,
                    s = 59,
                    onDateSelected = { viewModel.changerDateEnd(it) },
                )
            }
        }
    }
}



