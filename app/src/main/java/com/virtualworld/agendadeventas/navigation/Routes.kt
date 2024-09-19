package com.virtualworld.agendadeventas.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.ui.graphics.vector.ImageVector
import com.virtualworld.agendadeventas.R


sealed class Routess(val route: String) {
    object PantallaInicio: Routess("Resumen")
    object PantallaAgregar: Routess("Agregar")
    object PantallaEditar: Routess("Editar")
    object PantallaVender: Routess("Vender")
    object PantallaRegistro: Routess("Registro")
    object PantallaTiendas: Routess("Tiendas")
    object PantallaExportar: Routess("Exportar")



}


sealed class DrawerNavDestination(val route: String, @StringRes val title: Int, val contentDescription:String, val icon: ImageVector,)
{


     object Resumen : DrawerNavDestination(
        route = "Resumen",
        title =  R.string.menu_resumen,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Home,
    )

    object Agregar : DrawerNavDestination(
        route = "Agregar",
        title =  R.string.menu_agregar,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Add,
    )

    object Editar : DrawerNavDestination(
        route = "Editar",
        title =  R.string.menu_editar,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Edit,
    )

    object Vender : DrawerNavDestination(
        route = "Vender",
        title =  R.string.menu_vender,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.PointOfSale,
    )

    object Registro : DrawerNavDestination(
        route = "Registro",
        title =  R.string.menu_registro,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.ViewAgenda,
    )

    object Tiendas : DrawerNavDestination(
        route = "Tiendas",
        title =  R.string.menu_tiendas,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Storefront,
    )

    object Exportar : DrawerNavDestination(
        route = "Exportar",
        title =  R.string.menu_exportar,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.ImportExport,
    )

    companion object {
        fun getDestinations(): List<DrawerNavDestination> {
            return listOf(
                Resumen,
                Agregar,
                Editar,
                Vender,
                Registro,
                Tiendas,
                Exportar
            )
        }
    }

}

