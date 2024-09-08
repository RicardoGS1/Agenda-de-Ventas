package com.virtualword3d.salesregister.Screen.Exportar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel

import com.virtualworld.agendadeventas.R

import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualworld.agendadeventas.Screen.Inicio.TituloCard
import kotlinx.coroutines.launch


@Composable
fun PantallaExportar()
{

    val viewModel: ViewModelExportar = hiltViewModel()
    viewModel.onChangeAutentificado()

    Column(modifier = Modifier.fillMaxSize()) {

        Regiastrarse(viewModel)
        ExportarVenta(viewModel)
        ImportarVenta(viewModel)


    }
    DialogoConexion(viewModel)

}

@Composable
fun Regiastrarse(viewModel: ViewModelExportar)
{

    val identification: String by viewModel.autentificado.observeAsState(initial = "")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MaterialTheme.colors.primary),
    ) {

        if (identification == "" || identification == "null")
        {

            var nombreUsuario by remember { mutableStateOf("") }
            var contracena by remember { mutableStateOf("") }


            Column() {

                Text(
                    text = stringResource(id = R.string.export_registrarse),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = colorResource(R.color.white),
                )

                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.white)),

                          value = nombreUsuario,
                          onValueChange = { nombreUsuario = it },
                          label = { Text(text = stringResource(id = R.string.export_correo), color = colorResource(R.color.black)) })


                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(R.color.white)),
                    value = contracena, onValueChange = { contracena = it },
                    label = { Text(stringResource(id = R.string.export_contracena), color = colorResource(R.color.black)) },


                    )

                TextButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                           onClick = { viewModel.startSecion(nombreUsuario, contracena) }) {
                    Text(text = stringResource(id = R.string.export_iniciar_secion), color = colorResource(R.color.white)
                    )
                }

                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { viewModel.createUser(nombreUsuario, contracena) },
                ) {
                    Text(text = stringResource(id = R.string.crear_cuenta), color = colorResource(R.color.white)
                    )
                }

            }


        } else
        {

            Column() {

                Text(
                    text = stringResource(id = R.string.export_empresa),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = colorResource(R.color.white),
                )

                Text(
                    text = identification,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    fontSize = 20.sp,
                    color = colorResource(R.color.white),
                )

                TextButton(onClick = { viewModel.closeSecion() },
                           modifier = Modifier
                               .padding(horizontal = 8.dp)
                               .align(alignment = Alignment.End)
                ) {
                    Text(
                        text = stringResource(id = R.string.cerrar_sacion),
                        color = colorResource(R.color.white),
                    )
                }

            }
        }
    }
}

@Composable
fun ExportarVenta(viewModel: ViewModelExportar)
{


    val identification: String by viewModel.autentificado.observeAsState(initial = "")



    Card(modifier = Modifier
        .padding(8.dp)
        .padding(),
         border = BorderStroke(1.dp, Color.LightGray),
         shape = RoundedCornerShape(12.dp),
         elevation = 8.dp
    ) {
        Column() {

            //TITULO
            TituloCard(stringResource(id = R.string.card_exportar_ventas), MaterialTheme.colors.primaryVariant)

            Text(text = stringResource(id = R.string.card_exportar_ventas_text),
                 modifier = Modifier.padding(8.dp)
            )
            if (identification != "null" && identification != "")
            {
                TextButton(onClick = { viewModel.exportSales() },
                           modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.card_exportar_ventas_boton),
                    )

                }
            } else
            {
                Text(text = stringResource(id = R.string.nota_exportar_ventas_boton),
                     modifier = Modifier.padding(8.dp)
                )
            }

        }
    }
}


@Composable
fun ImportarVenta(viewModel: ViewModelExportar)
{

    val identification: String by viewModel.autentificado.observeAsState(initial = "")

    Card(modifier = Modifier
        .padding(8.dp)
        .padding(),
         border = BorderStroke(1.dp, Color.LightGray),
         shape = RoundedCornerShape(12.dp),
         elevation = 8.dp
    ) {

        Column() {

            //TITULO
            TituloCard(stringResource(id = R.string.card_importar_ventas), MaterialTheme.colors.primaryVariant)

            Text(text = stringResource(id = R.string.card_importar_ventas_text),
                 modifier = Modifier.padding(8.dp)
            )

            if (identification != "null" && identification != "")
            {
                TextButton(onClick = { viewModel.importSales() },
                           modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.card_importar_ventas_boton),
                    )

                }
            } else
            {
                Text(text = stringResource(id = R.string.nota_exportar_ventas_boton),
                     modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun DialogoConexion(viewModel: ViewModelExportar)
{

    val mensaje: Mensajes by viewModel.stateCall.observeAsState(Mensajes.NEUTRO)
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    SnackbarHost(hostState = snackState)

    fun iniciarSnakbar(texto: String)
    {
        scope.launch {
            snackState.showSnackbar(message = texto, duration = SnackbarDuration.Short
            )
        }
    }

    if (mensaje != Mensajes.NEUTRO)
    {

        if (mensaje == Mensajes.CARGANDO)
        {
            Dialog(onDismissRequest = {  }) {
                CircularProgressIndicator()
            }
        }

        if (mensaje == Mensajes.ERROR)
        {

            AlertDialog(onDismissRequest = {
                viewModel.onChangeLoading(Mensajes.NEUTRO)
            },
                        confirmButton = { },
                        dismissButton = {
                            TextButton(onClick = {
                                viewModel.onChangeLoading(Mensajes.NEUTRO)
                            }) {
                                Text(text = "ok")
                            }
                        },
                        title = { Text(text = "Error") },
                        text = { Text(text = stringResource(id = R.string.mensaje_error)) })

        }

        if (mensaje == Mensajes.BIEN)
        {


            iniciarSnakbar(stringResource(id = R.string.mensaje_exitoso_export))
            viewModel.onChangeLoading(Mensajes.NEUTRO)


        }
    }
}
