package com.virtualworld.agendadeventas.ui.screen.resume

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    infoDescription: String,
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

            TituloCard(
                titulo = title,
                color = MaterialTheme.colorScheme.primary,
                infoDescription = infoDescription,
            )

            InfoRow(
                stringResource(id = R.string.inicio_info_tiendas),
                colum2
            )

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
fun TituloCard(titulo: String, color: Color, infoDescription: String) {
    var showInfoDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        Text(
            text = titulo,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .padding(top = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = color,
        )

        IconButton(
            onClick = { showInfoDialog = true },
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.inicio_info_icon),
                tint = color,
            )
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(text = titulo) },
            text = { Text(text = infoDescription) },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(text = stringResource(id = R.string.inicio_info_cerrar))
                }
            },
        )
    }
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
                fontWeight = FontWeight.Medium
            )
            Text(
                text = valorTienda,
                modifier = Modifier.padding(end = 16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
