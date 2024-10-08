package com.virtualworld.agendadeventas.ui.Screen.record

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.Model.SoldForStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ListProductRecordView (stateScreenProductStore: List<SoldForStore>,) {


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 46.dp)
    ) {

        itemsIndexed(items = stateScreenProductStore) { index, nombres ->


            TtemProducto(index, nombres.nombre, nombres.unidades, nombres.compra, nombres.valor, nombres.fecha.toString(), )
        }
    }
}



@Composable
fun TtemProducto(index: Int, nombres: String, unidades: Int, costoProducto: Long, precioProducto: Long, fechaVenta: String ) {

    Log.d("efecto", "TtemProducto $index")

    Card(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        //backgroundColor = MaterialTheme.colors.surface,
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
                text = stringResource(id = R.string.record_unidades) + " $unidades  " + stringResource(
                    id = R.string.record_costo
                ) + " $costoProducto  " + stringResource(id = R.string.record_precio) + " $precioProducto",
                fontSize = 16.sp,
            )

            Text(
                modifier = Modifier.align(alignment = Alignment.End),
                text = stringResource(id = R.string.record_fecha) + convertMillisecondsToDate(fechaVenta),
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

fun convertMillisecondsToDate(milliseconds: String): String {
    val date = Date(milliseconds.toLong())
    val formatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    return formatter.format(date)
}