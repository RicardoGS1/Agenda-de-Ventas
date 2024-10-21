package com.virtualword3d.salesregister.ViewMain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ViewAgenda

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R

@Composable
fun DrawerHeader()
{
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primary)
        .padding(vertical = 0.dp), contentAlignment = Alignment.Center


    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_drawer),
            contentDescription = "imagen "+stringResource(id = R.string.menu_banel),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.FillBounds,
        )

        Text(text = stringResource(id = R.string.menu_banel),
             fontSize = 28.sp,
             color = Color.White,
             fontWeight = FontWeight.Normal,
             fontFamily = FontFamily.Serif,
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(start = 16.dp)

        )
    }
}



@Composable
fun DrawerBody(onItemClick: (MenuItem) -> Unit)
{

    val items = listOf(MenuItem(id = stringResource(id = R.string.menu_resumen),
                                title = stringResource(id = R.string.menu_resumen),
                                contentDescription = "go to screen Resumen",
                                icon = Icons.Default.Home
    ),
                       MenuItem(id = stringResource(id = R.string.menu_agregar),
                                title = stringResource(id = R.string.menu_agregar),
                                contentDescription = "go to screen Agregar",
                                icon = Icons.Default.Add
                       ),
                       MenuItem(id = stringResource(id = R.string.menu_editar),
                                title = stringResource(id = R.string.menu_editar),
                                contentDescription = "go to screen Editar",
                                icon = Icons.Default.Edit
                       ),
                       MenuItem(id = stringResource(id = R.string.menu_vender),
                                title = stringResource(id = R.string.menu_vender),
                                contentDescription = "go to screen Vender",
                                icon = Icons.Default.PointOfSale
                       ),
                       MenuItem(id = stringResource(id = R.string.menu_registro),
                                title = stringResource(id = R.string.menu_registro),
                                contentDescription = "go to screen Registro",
                                icon = Icons.Default.ViewAgenda
                       ),
                       MenuItem(id = stringResource(id = R.string.menu_tiendas),
                                title = stringResource(id = R.string.menu_tiendas),
                                contentDescription = "go to screen Tiendas",
                                icon = Icons.Default.Storefront
                       ),
                       MenuItem(id = stringResource(id = R.string.menu_exportar),
                                title = stringResource(id = R.string.menu_exportar),
                                contentDescription = "go to screen Exportar",
                                icon = Icons.Default.ImportExport
                       )
    )


    Column(modifier = Modifier.padding(8.dp)
    ) {
        items.forEach { item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(item) }
                .padding(16.dp)) {
                Icon(imageVector = item.icon, contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(modifier = Modifier.weight(1f), text = item.title
                )
            }
        }
    }
}