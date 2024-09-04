package com.virtualword3d.salesregister.Screen.Registro

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R

@Composable
fun PantallaRegistro() {

    val viewModel: RegistroViewModel = hiltViewModel()
    viewModel.getTiendas()
    viewModel.allVendidos()

    Box() {
        Column(modifier = Modifier.fillMaxWidth()) {

            TiendasDropdownMenu(viewModel)
            ListaProductosVenta(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TiendasDropdownMenu(viewMode: RegistroViewModel) {

    val nombreTienda1: String by viewMode.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewMode.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewMode.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewMode.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewMode.nombreTienda5.observeAsState(initial = "")

    val selectedType: String by viewMode.tiendaSeleccionada.observeAsState(initial = "")
    val types = listOf(nombreTienda1, nombreTienda2, nombreTienda3, nombreTienda4, nombreTienda5)
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier.fillMaxWidth(),
    ) {


        TextField(
            readOnly = true,
            value = selectedType,
            onValueChange = { },
            label = { Text(stringResource(R.string.label_spiner_rejistro)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            types.forEach { selectionOption ->

                if (selectionOption != "") {
                    DropdownMenuItem(
                        onClick = {
                            viewMode.changeTienda(selectionOption)
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }

            }

        }
    }

}

@Composable
fun ListaProductosVenta(viewMode: RegistroViewModel) {

    val nombreProducto: List<String> by viewMode.nombreProducto.observeAsState(initial = listOf())
    val unidadesProducto: List<Int> by viewMode.unidadesProducto.observeAsState(initial = listOf())
    val costoProducto: List<Long> by viewMode.costoProducto.observeAsState(initial = listOf())
    val precioProducto: List<Long> by viewMode.precioProducto.observeAsState(initial = listOf())
    val fechaVenta: List<String> by viewMode.fechaVenta.observeAsState(initial = listOf())

    Log.d("efecto", "ListaProductosVenta  $nombreProducto")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 46.dp)
    ) {

        itemsIndexed(items = nombreProducto) { index, nombres ->
            TtemProducto(index, nombres, unidadesProducto[index],costoProducto[index],precioProducto[index],fechaVenta[index] ,viewMode)
        }
    }
}

@Composable
fun TtemProducto(index: Int, nombres: String, unidades: Int, costoProducto: Long, precioProducto: Long, fechaVenta: String ,viewMode: RegistroViewModel) {

    Log.d("efecto", "TtemProducto $index")

    Card(
        modifier=Modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
          ) {

        Column() {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                text = nombres,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold

            )

            Text(
                modifier = Modifier.align(alignment = Alignment.End),
                text = stringResource(id = R.string.record_unidades)+" $unidades  " + stringResource(id = R.string.record_costo) + " $costoProducto  " + stringResource(id = R.string.record_precio) + " $precioProducto",
                fontSize = 16.sp,
                )

            Text(
                modifier = Modifier.align(alignment = Alignment.End),
                text = stringResource(id = R.string.record_fecha) + " $fechaVenta",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}




