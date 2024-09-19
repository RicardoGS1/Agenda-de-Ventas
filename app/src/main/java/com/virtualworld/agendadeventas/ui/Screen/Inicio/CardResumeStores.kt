package com.virtualworld.agendadeventas.ui.Screen.Inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R


@Composable
fun CardResumeStores(
    title: String,
    colum2: String,
    detalleCol1: List<String>,
    detalleCol2: List<String>,
) {

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
                title,
                MaterialTheme.colorScheme.primary
            )

            //INFO
            InfoRow(
                stringResource(id = R.string.inicio_info_tiendas),
                colum2
            )

            //TIENDAS

            for (i in detalleCol1.indices) {
                ItemTienda(detalleCol1[i], detalleCol2[i])
            }


        }
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

