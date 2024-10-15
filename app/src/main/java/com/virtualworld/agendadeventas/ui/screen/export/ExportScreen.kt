package com.virtualworld.agendadeventas.ui.screen.export

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel

import com.virtualworld.agendadeventas.R

import com.virtualworld.agendadeventas.core.entity.ScreenUiState

import kotlinx.coroutines.launch


@Composable
fun ExportScreen() {

    val viewModel: ExportViewModel = hiltViewModel()
    val identification: String by viewModel.identification.collectAsState()

    val authenticateUser = { userName: String, password: String, isNewUser: Boolean ->
        viewModel.authenticateUser(userName, password, isNewUser)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        LogInUpClose(identification, authenticateUser) { viewModel.closeSession() }
        ExportSales(identification) { viewModel.exportSales() }
        ImportSales(identification) { viewModel.importSales() }

    }
    DialogoConexion(viewModel)
}

@Composable
fun LogInUpClose(
    identification: String,
    authenticateUser: (nameUser: String, password: String, isNewUser: Boolean) -> Unit,
    closeSession: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        if (identification == "" || identification == "null") {

            val userName by remember { mutableStateOf("") }
            val password by remember { mutableStateOf("") }

            LoginSignupSection(userName, password, authenticateUser)

        } else {
            CompanyInfoSection(identification, closeSession)
        }
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
private fun LoginSignupSection(
    userName: String,
    pasword: String,
    authenticateUser: (nameUser: String, password: String, isNewUser: Boolean) -> Unit
) {
    var userName1 = userName
    var pasword1 = pasword
    Column() {

        Text(
            text = stringResource(id = R.string.export_registrarse),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            value = userName1,
            onValueChange = { userName1 = it },
            label = {
                Text(text = stringResource(id = R.string.export_correo))
            })


        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            value = pasword1,
            onValueChange = { pasword1 = it },
            label = {
                Text(stringResource(id = R.string.export_contracena))
            })

        TextButton(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { authenticateUser(userName1, pasword1, false) }) {
            Text(
                text = stringResource(id = R.string.export_iniciar_secion),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { authenticateUser(userName1, pasword1, true) },
        ) {
            Text(
                text = stringResource(id = R.string.crear_cuenta),
                color = MaterialTheme.colorScheme.onPrimary
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

@Composable
fun DialogoConexion(viewModel: ExportViewModel) {

    val mensaje: ScreenUiState by viewModel.screenUiState.collectAsState()
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    SnackbarHost(hostState = snackState)

    fun iniciarSnakbar(texto: String) {
        scope.launch {
            snackState.showSnackbar(
                message = texto, duration = SnackbarDuration.Short
            )
        }
    }

    if (mensaje != ScreenUiState.NEUTRAL) {

        if (mensaje == ScreenUiState.LOADING) {
            Dialog(onDismissRequest = { }) {
                CircularProgressIndicator()
            }
        }

        if (mensaje == ScreenUiState.ERROR) {

            AlertDialog(onDismissRequest = {
                viewModel.onChangerScreenState(ScreenUiState.NEUTRAL)
            },
                confirmButton = { },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.onChangerScreenState(ScreenUiState.NEUTRAL)
                    }) {
                        Text(text = "ok")
                    }
                },
                title = { Text(text = "Error") },
                text = { Text(text = stringResource(id = R.string.mensaje_error)) })

        }

        if (mensaje == ScreenUiState.OK) {


            iniciarSnakbar(stringResource(id = R.string.mensaje_exitoso_export))
            viewModel.onChangerScreenState(ScreenUiState.NEUTRAL)


        }
    }
}
