package com.virtualword3d.salesregister.Screen.Tiendas

import android.util.Log
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.entity.ScreenUiState
import kotlinx.coroutines.launch


@Composable
fun PantallaTiendas() {
    
    val viewModel: ViewModelTienda = hiltViewModel()
    viewModel.getTiendas()
    ModificarTiendas(viewModel)
    MensajesAgregar(viewModel)
}

@Composable
fun ModificarTiendas(viewModel: ViewModelTienda) {

    //VARIABLES ESTADOS
    val nombreTienda1: String by viewModel.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewModel.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewModel.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewModel.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewModel.nombreTienda5.observeAsState(initial = "")

    val shwitTienda1: Boolean by viewModel.shwitTienda1.observeAsState(initial = true)
    val shwitTienda2: Boolean by viewModel.shwitTienda2.observeAsState(initial = true)
    val shwitTienda3: Boolean by viewModel.shwitTienda3.observeAsState(initial = true)
    val shwitTienda4: Boolean by viewModel.shwitTienda4.observeAsState(initial = true)
    val shwitTienda5: Boolean by viewModel.shwitTienda5.observeAsState(initial = true)

    //VISTA PRINCIPAL
    Box() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            //TITULO
            item {
                Text(
                    text = stringResource(id = R.string.titulo_Tiendas),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    //fontWeight = FontWeight.Bold,
                   // color = MaterialTheme.colors.onSurface
                )
            }

            //INFO COLUMNA
            item {
                Box(modifier = Modifier.fillMaxWidth()) {


                    Text(
                        text = stringResource(id = R.string.info_colunas_nombre),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    Text(
                        text = stringResource(id = R.string.info_colunas_activas),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .padding(end = 8.dp)
                    )
                }
            }

            //TIENDA1
            item {
                FildTienda(
                    nombreTienda1,
                    shwitTienda1,
                    { viewModel.OnChangedTienda1(it) },
                    { viewModel.OnChangedShwitTienda1(it) },
                " 1")
            }

            //TIENDA2
            item {
                FildTienda(nombreTienda2,
                           shwitTienda2,
                           { viewModel.OnChangedTienda2(it) },
                           { viewModel.OnChangedShwitTienda2(it) },
                           " 2"
                )
            }

            //TIENDA3
            item {
                FildTienda(nombreTienda3,
                           shwitTienda3,
                           { viewModel.OnChangedTienda3(it) },
                           { viewModel.OnChangedShwitTienda3(it) },
                           " 3"
                )
            }

            //TIENDA4
            item {
                FildTienda(nombreTienda4,
                           shwitTienda4,
                           { viewModel.OnChangedTienda4(it) },
                           { viewModel.OnChangedShwitTienda4(it) },
                           " 4"
                )
            }

            //TIENDA5
            item {
                FildTienda(nombreTienda5,
                           shwitTienda5,
                           { viewModel.OnChangedTienda5(it) },
                           { viewModel.OnChangedShwitTienda5(it) },
                           " 5"
                )
            }

        }

        Button(
            onClick = { viewModel.setTiendas() },
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(text = stringResource(id = R.string.boton_guardar_Tiendas))

        }

    }
}



@Composable
fun FildTienda(nombreTienda: String,
               shwitTienda: Boolean,
               onTextFieldChanged: (String) -> Unit,
               OnChangedShwitTienda1: (Boolean) -> Unit,
               s: String) {


    Row() {


        TextField(
            value = nombreTienda,
            onValueChange = { onTextFieldChanged(it) },
            label = {
                Text(
                    text = stringResource(id = R.string.label_nombre_de_tiendas)+s,

                    )
            },
            modifier = Modifier

                .padding(horizontal = 32.dp)
                .padding(vertical = 8.dp),
            singleLine = true


        )


        Switch(
            checked = shwitTienda, onCheckedChange = { OnChangedShwitTienda1(it) },
            modifier = Modifier
                .align(alignment = CenterVertically)
                .padding(end = 32.dp),


        )

    }
}


@Composable
fun MensajesAgregar(viewModel: ViewModelTienda) {
    Log.d("efecto","MensajesAgregar")

    val respuestaError: ScreenUiState by viewModel.respuestaError.observeAsState(ScreenUiState.NEUTRAL)
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SnackbarHost(hostState = snackState)

    fun iniciarSnakbar(texto: String) {
        scope.launch {
            snackState.showSnackbar(
                message = texto,
                duration = SnackbarDuration.Short
            )
        }
    }

    if (respuestaError == ScreenUiState.ERROR) {

        AlertDialog(onDismissRequest = { viewModel.controlMensaje(ScreenUiState.NEUTRAL) },
            confirmButton = {
                TextButton(onClick = { viewModel.controlMensaje(ScreenUiState.NEUTRAL) }) {
                    Text(text = stringResource(id = R.string.boton_mensaje_confirmar))
                }
            },
            title = { Text(text = stringResource(id = R.string.mensaje_titulo_error_tiendas)) },
            text = { Text(text = stringResource(id = R.string.mensaje_error_tiendas)) }
        )
    }

    if (respuestaError == ScreenUiState.OK) {

        iniciarSnakbar(stringResource(id = R.string.mensaje_guardado_tiendas))
        viewModel.controlMensaje(ScreenUiState.NEUTRAL)
    }

}