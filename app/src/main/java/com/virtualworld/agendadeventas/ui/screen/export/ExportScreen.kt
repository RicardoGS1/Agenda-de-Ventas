package com.virtualworld.agendadeventas.ui.screen.export

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView

import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState


@Composable
fun ExportScreen() {

    val viewModel: ExportViewModel = hiltViewModel()
    val identification: String by viewModel.identification.collectAsState()
    val screenUiState: ScreenUiState by viewModel.screenUiState.collectAsState()

    val authenticateUser = { userName: String, password: String, isNewUser: Boolean ->
        viewModel.authenticateUser(userName, password, isNewUser)
    }

    val onChangerScreenUiState = { stateUi: ScreenUiState -> viewModel.changerUiState(stateUi) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {

                if (identification == "" || identification == "null")
                    LogInUp(authenticateUser)
                else
                    CompanyInfoSection(identification) { viewModel.closeSession() }
            }

            ExportSales(identification) { viewModel.exportData() }
            ImportSales(identification) { viewModel.importSales() }

        }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChangerScreenUiState
        )

    }
}



@Composable
private fun CompanyInfoSection(identification: String, closeSession: () -> Unit) {
    Column() {

        Text(
            text = stringResource(id = R.string.export_empresa),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            text = identification,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onPrimary

            )

        TextButton(
            onClick = { closeSession() },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(alignment = Alignment.End)
        ) {
            Text(
                text = stringResource(id = R.string.cerrar_sacion),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

    }
}


@Composable
fun ExportSales(identification: String, exportSales: () -> Unit) {


    Card(
        modifier = Modifier
            .padding(8.dp)
            .padding(),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(12.dp),
        //elevation = 8.dp
    ) {
        Column() {

            //TITULO
            Text(
                text = stringResource(id = R.string.card_exportar_ventas), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )


            Text(
                text = stringResource(id = R.string.card_exportar_ventas_text),
                modifier = Modifier.padding(8.dp)
            )
            if (identification != "null" && identification != "") {
                TextButton(
                    onClick = { exportSales() },
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.card_exportar_ventas_boton),
                    )

                }
            } else {
                Text(
                    text = stringResource(id = R.string.nota_exportar_ventas_boton),
                    modifier = Modifier.padding(8.dp)
                )
            }

        }
    }
}


@Composable
fun ImportSales(identification: String, importSales: () -> Unit) {


    Card(
        modifier = Modifier
            .padding(8.dp)
            .padding(),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(12.dp),
        //elevation = 8.dp
    ) {

        Column() {

            //TITULO
            Text(
                text = stringResource(id = R.string.card_importar_ventas), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )


            Text(
                text = stringResource(id = R.string.card_importar_ventas_text),
                modifier = Modifier.padding(8.dp)
            )

            if (identification != "null" && identification != "") {
                TextButton(
                    onClick = { importSales() },
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.card_importar_ventas_boton),
                    )

                }
            } else {
                Text(
                    text = stringResource(id = R.string.nota_exportar_ventas_boton),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

