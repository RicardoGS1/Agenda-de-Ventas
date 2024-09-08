package com.virtualword3d.salesregister.Screen.Venta

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.virtualworld.agendadeventas.R
import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualworld.agendadeventas.Screen.Venta.ViewModelVender
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaVender(){

    val viewModel: ViewModelVender = hiltViewModel()
    viewModel.getTiendas()
    viewModel.getProducto()
    MensajesAgregar(viewModel)

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxWidth())
        {
            TiendasDropdownMenu(viewModel)
            ListaProductosVenta(viewModel)
        }

        Button( modifier = Modifier.align(alignment = Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500)) ,
                onClick = { viewModel.GuardarVenta() } ) {
                Text(text = stringResource(id = R.string.boton_guardar_vender))
        }

    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendasDropdownMenu(viewModel: ViewModelVender) {

    val nombreTienda1: String by viewModel.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewModel.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewModel.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewModel.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewModel.nombreTienda5.observeAsState(initial = "")
   // val selectedType: String by viewModel.tiendaSeleccionada.observeAsState(initial = "")
    val types = listOf(nombreTienda1, nombreTienda2, nombreTienda3, nombreTienda4, nombreTienda5)
    var selectedType by remember { mutableStateOf(types[0])}
    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = selectedType,
            onValueChange = { },
            label = { Text(stringResource(id = R.string.label_spiner_vender)) },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.textFieldColors(), // Cambiado a Material 3
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor() // Añadido para Material 3
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { selectionOption ->
                if (selectionOption != "") {
                    DropdownMenuItem(
                        text = { Text(selectionOption) }, // Añadido text
                        onClick = {
                            viewModel.changeTienda(selectionOption)
                            selectedType = selectionOption // Actualiza el valor seleccionado
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ListaProductosVenta(viewModel: ViewModelVender) {

    val listaNombreProducto: List<String> by viewModel.nombreProducto.observeAsState(initial = listOf())
    val listaUnidades:List<Int> by viewModel.listaUnidades.observeAsState(initial = listOf())

    Log.d("efecto", "ListaProductosVenta$listaUnidades")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 46.dp)
    ) {

        itemsIndexed(items = listaNombreProducto) { index, nombres ->
            TtemProducto(index, nombres,viewModel )
        }
    }
}


@Composable
fun TtemProducto(index:Int, nombres: String, viewModel: ViewModelVender){

    val listaUnidades:List<Int> by viewModel.listaUnidades.observeAsState(initial = listOf())
    val actualisarcompouse:Int by viewModel.actualisarcompouse.observeAsState(initial = 0)

    Log.e("efecto", "TtemProducto  $listaUnidades  $actualisarcompouse")

    Card(modifier = Modifier.padding(4.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                text = nombres,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold

                )


            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = {
                        viewModel.resUnidades(index)
                        viewModel.actualisarya()
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500)) ,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(0.dp)
                ) {
                    Text(text = "-", fontSize = 32.sp)
                }

                Text(

                    text = listaUnidades[index].toString(),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    )

                Button(
                    onClick = {
                        viewModel.addUnidades(index)
                        viewModel.actualisarya()
                    },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_500)) ,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(0.dp),
                ) {
                    Text(text = "+", fontSize = 32.sp)

                }
            }
        }
    }
}


@Composable
fun MensajesAgregar(viewModel: ViewModelVender) {
    Log.d("efecto","MensajesAgregar")

    val respuestaError: Mensajes by viewModel.respuestaError.observeAsState(Mensajes.NEUTRO)
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

    if (respuestaError == Mensajes.BIEN) {
        iniciarSnakbar(stringResource(id = R.string.mensaje_vendido_vender))
        viewModel.controlMensaje(Mensajes.NEUTRO)
    }

}


