package com.virtualworld.agendadeventas.Screen.Inicio

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualword3d.salesregister.Screen.Inicio.ViewModelInicio
import com.virtualworld.agendadeventas.R

import java.util.Calendar


@Composable
fun PantallaInicio(viewModel: ViewModelInicio = hiltViewModel()) {

    viewModel.getTiendas()
    viewModel.getUnidades()
    viewModel.getGanancia()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
    ) {

        SeleccionarPeriodos(viewModel)

        LazyColumn() {

            item {
                UnidadesVendida(viewModel)
            }

            item {
                GananciaVenta(viewModel)
            }
        }
    }
}


@Composable
fun SeleccionarPeriodos(viewModel: ViewModelInicio) {


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

                BotonDesde(Modifier.align(Alignment.CenterStart), viewModel)
                BotonHasta(Modifier.align(Alignment.CenterEnd), viewModel)
            }
        }
    }
}


@Composable
fun UnidadesVendida(viewModel: ViewModelInicio) {


    val nombreTienda1: String by viewModel.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewModel.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewModel.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewModel.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewModel.nombreTienda5.observeAsState(initial = "")

    val unidadesTienda1: Int by viewModel.unidadesTienda1.observeAsState(initial = 0)
    val unidadesTienda2: Int by viewModel.unidadesTienda2.observeAsState(initial = 0)
    val unidadesTienda3: Int by viewModel.unidadesTienda3.observeAsState(initial = 0)
    val unidadesTienda4: Int by viewModel.unidadesTienda4.observeAsState(initial = 0)
    val unidadesTienda5: Int by viewModel.unidadesTienda5.observeAsState(initial = 0)


    Card(
        modifier = Modifier
            .padding(8.dp)
            .padding(),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation()
    ) {

        Column {

            //TITULO
            TituloCard(
                stringResource(id = R.string.inicio_unidades_vendidas),
                MaterialTheme.colorScheme.primary
            )

            //INFO
            InfoRow(
                stringResource(id = R.string.inicio_info_tiendas),
                stringResource(id = R.string.inicio_info_unidades)
            )

            //TIENDAS
            ItemTienda(nombreTienda1, unidadesTienda1.toString())

            ItemTienda(nombreTienda2, unidadesTienda2.toString())

            ItemTienda(nombreTienda3, unidadesTienda3.toString())

            ItemTienda(nombreTienda4, unidadesTienda4.toString())

            ItemTienda(nombreTienda5, unidadesTienda5.toString())
        }
    }
}

@Composable
fun GananciaVenta(viewModel: ViewModelInicio) {

    val nombreTienda1: String by viewModel.nombreTienda1.observeAsState(initial = "")
    val nombreTienda2: String by viewModel.nombreTienda2.observeAsState(initial = "")
    val nombreTienda3: String by viewModel.nombreTienda3.observeAsState(initial = "")
    val nombreTienda4: String by viewModel.nombreTienda4.observeAsState(initial = "")
    val nombreTienda5: String by viewModel.nombreTienda5.observeAsState(initial = "")

    val gananciaTienda1: Long by viewModel.gananciaTienda1.observeAsState(initial = 0)
    val gananciaTienda2: Long by viewModel.gananciaTienda2.observeAsState(initial = 0)
    val gananciaTienda3: Long by viewModel.gananciaTienda3.observeAsState(initial = 0)
    val gananciaTienda4: Long by viewModel.gananciaTienda4.observeAsState(initial = 0)
    val gananciaTienda5: Long by viewModel.gananciaTienda5.observeAsState(initial = 0)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .padding(),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column {

            //TITULO
            TituloCard(
                stringResource(id = R.string.inicio_ganancia),
                MaterialTheme.colorScheme.primary
            )

            //INFO
            InfoRow(
                text1 = stringResource(id = R.string.inicio_info_tiendas),
                text2 = stringResource(id = R.string.inicio_info_ganancia)
            )

            ItemTienda(nombreTienda = nombreTienda1, valorTienda = gananciaTienda1.toString())

            ItemTienda(nombreTienda = nombreTienda2, valorTienda = gananciaTienda2.toString())

            ItemTienda(nombreTienda = nombreTienda3, valorTienda = gananciaTienda3.toString())

            ItemTienda(nombreTienda = nombreTienda4, valorTienda = gananciaTienda4.toString())

            ItemTienda(nombreTienda = nombreTienda5, valorTienda = gananciaTienda5.toString())

        }

    }

}


@Composable
fun BotonHasta(modifier: Modifier, viewModel: ViewModelInicio) {


    val dateHasta: String by viewModel.dateHasta.observeAsState("")


    val year: Int
    val month: Int
    val day: Int

    val calender = Calendar.getInstance()
    year = calender.get(Calendar.YEAR)
    month = calender.get(Calendar.MONTH) + 1
    day = calender.get(Calendar.DAY_OF_MONTH)
    // calender.time = Date()


    val datePickerDialog = DatePickerDialog(
        LocalContext.current,

        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->

            viewModel.changeDateHasta("$dayOfMonth/$month/$year")
            viewModel.getUnidades()
            viewModel.getGanancia()


        }, year, month, day

    )

    OutlinedButton(
        onClick = { datePickerDialog.show() },
        modifier = modifier
            .padding(end = 10.dp)
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(50),
        //border = BorderStroke(width = 1.5.dp, colorResource(R.color.purple_700)),

        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)


    ) {
        Text(
            text = stringResource(id = R.string.inicio_hasta) + " $dateHasta",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primaryContainer,
        )
    }


}

@Composable
fun BotonDesde(modifier: Modifier, viewModel: ViewModelInicio) {

    val dateDesde: String by viewModel.dateDesde.observeAsState("")

    val year: Int
    val month: Int
    val day: Int

    val calender = Calendar.getInstance()
    year = calender.get(Calendar.YEAR)
    month = calender.get(Calendar.MONTH) + 1
    day = calender.get(Calendar.DAY_OF_MONTH)


    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            viewModel.changeDateDesde("$dayOfMonth/$month/$year")
            viewModel.getUnidades()
            viewModel.getGanancia()
        }, year, month, day

    )



    OutlinedButton(
        onClick = {
            datePickerDialog.show()
        },
        modifier = modifier
            .padding(start = 10.dp)
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(50),
        //border = BorderStroke(width = 1.5.dp, colorResource(R.color.purple_700)),

        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)
        // ButtonDefaults.buttonColors( Color.White)
    ) {
        Text(
            stringResource(id = R.string.inicio_desde) + " $dateDesde",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primaryContainer,
        )
    }


}


@Composable
fun InfoRow(text1: String, text2: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text1,
            fontWeight = FontWeight.Light,
        )
        Text(
            text = text2,
            fontWeight = FontWeight.Light,
        )
    }
}

@Composable
fun TituloCard(titulo: String, color: Color) {

    Text(
        text = titulo, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = color
    )
}

@Composable
fun ItemTienda(nombreTienda: String, valorTienda: String) {

    if (nombreTienda != "") {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = nombreTienda,
                modifier = Modifier,
                fontSize = 20.sp,
                //color = colorResource(R.color.)
                fontWeight = FontWeight.Medium

            )
            Text(
                text = valorTienda.toString(),
                modifier = Modifier.padding(end = 16.dp),
                fontSize = 20.sp,
                //color = colorResource(R.color.)
                fontWeight = FontWeight.Medium
            )
        }
    }
}


