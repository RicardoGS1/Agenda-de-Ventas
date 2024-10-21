package com.virtualword3d.salesregister.Screen.Agregar

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R


import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState

import kotlinx.coroutines.launch


@Composable
fun PantallaAgregar() {
    Log.d("efecto","PantallaAgregar")

    val viewModel: ViewModelAgregar = hiltViewModel()
    viewModel.getTiendas()
    AgregarProductos(viewModel)
    MensajesAgregar(viewModel)

}


@Composable
fun AgregarProductos(viewModel: ViewModelAgregar) {
    Log.d("efecto","AgregarProductos")


    val nombreProducto: String by viewModel.nombreProducto.observeAsState(initial = "")
    val precioCompra: String by viewModel.precioCompra.observeAsState(initial = "")
    val precioTienda1: String by viewModel.precioTienda1.observeAsState(initial = "")
    val precioTienda2: String by viewModel.precioTienda2.observeAsState(initial = "")
    val precioTienda3: String by viewModel.precioTienda3.observeAsState(initial = "")
    val precioTienda4: String by viewModel.precioTienda4.observeAsState(initial = "")
    val precioTienda5: String by viewModel.precioTienda5.observeAsState(initial = "")

    val activaTienda1: Boolean by viewModel.activaTienda1.observeAsState(initial = true)
    val activaTienda2: Boolean by viewModel.activaTienda2.observeAsState(initial = true)
    val activaTienda3: Boolean by viewModel.activaTienda3.observeAsState(initial = true)
    val activaTienda4: Boolean by viewModel.activaTienda4.observeAsState(initial = true)
    val activaTienda5: Boolean by viewModel.activaTienda5.observeAsState(initial = true)

    val nombreTienda1: String by viewModel.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewModel.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewModel.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewModel.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewModel.nombreTienda5.observeAsState(initial = "")


    Box() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //TITULO
            item {
                Text(
                    text = stringResource(id = R.string.titulo_agregar),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    //fontWeight = FontWeight.Bold,
                    //color = MaterialTheme.colors.onSurface,


                )
            }

            //NOMBRE PRODUCTO
            item {
                TextField(
                    value = nombreProducto,
                    onValueChange = { viewModel.OnChangedNombreProducto(it) },
                    label = { Text(text = stringResource(id = R.string.label_nombre_producto)) },
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(vertical = 8.dp),
                    singleLine = true
                )
            }

            //PRECIO COMPRA
            item {
                TextField(
                    value = precioCompra,
                    onValueChange = { viewModel.OnChangedPrecioCompra(it) },
                    label = { Text(text = stringResource(id = R.string.label_precio_compra)) },
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }

            //TIENDA1
            if (activaTienda1) {
                item {
                    TextField(
                        value = precioTienda1,
                        onValueChange = { viewModel.OnChangedPrecioTienda1(it) },
                        label = { Text(text = stringResource(id = R.string.label_precio_venta) + " $nombreTienda1") },
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            if (activaTienda2) {
                item {
                    TextField(
                        value = precioTienda2,
                        onValueChange = { viewModel.OnChangedPrecioTienda2(it) },
                        label = { Text(text = stringResource(id = R.string.label_precio_venta) + " $nombreTienda2") },
                        modifier = Modifier

                            .padding(horizontal = 32.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            if (activaTienda3) {
                item {
                    TextField(
                        value = precioTienda3,
                        onValueChange = { viewModel.OnChangedPrecioTienda3(it) },
                        label = { Text(text = stringResource(id = R.string.label_precio_venta) + " $nombreTienda3") },
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            if (activaTienda4) {
                item {
                    TextField(
                        value = precioTienda4,
                        onValueChange = { viewModel.OnChangedPrecioTienda4(it) },
                        label = { Text(text = stringResource(id = R.string.label_precio_venta) + " $nombreTienda4") },
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            if (activaTienda5) {
                item {
                    TextField(
                        value = precioTienda5,
                        onValueChange = { viewModel.OnChangedPrecioTienda5(it) },
                        label = { Text(text = stringResource(id = R.string.label_precio_venta) + " $nombreTienda5") },
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                }
            }
        }


        Button(
            onClick = { viewModel.setProducto() },
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),


        ) {
            Text(text = stringResource(id = R.string.boton_guardar_agregar), color = MaterialTheme.colorScheme.primaryContainer)
        }
    }
}



@Composable
fun MensajesAgregar(viewModel: ViewModelAgregar) {
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

        AlertDialog(
            onDismissRequest = { viewModel.controlMensaje(ScreenUiState.NEUTRAL) },
            confirmButton = {
                TextButton(onClick = { viewModel.controlMensaje(ScreenUiState.NEUTRAL) }) {
                    Text(text = stringResource(id = R.string.boton_mensaje_confirmar), color = MaterialTheme.colorScheme.primary)
                }
            },
            title = { Text(text = stringResource(id = R.string.titulo_mensaje_error)) },
            text = { Text(text = stringResource(id = R.string.text_mensaje_error)) }
        )
    }

    if (respuestaError == ScreenUiState.OK) {

        iniciarSnakbar(stringResource(id = R.string.text_mensaje_agregado))
        viewModel.reiniciarValores()
        viewModel.controlMensaje(ScreenUiState.NEUTRAL)
    }

    }











