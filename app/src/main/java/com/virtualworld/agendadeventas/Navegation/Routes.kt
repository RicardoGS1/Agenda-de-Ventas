package com.virtualworld.agendadeventas.Navegation

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


sealed class Routes(val route: String) {
    object PantallaInicio: Routes("Resumen")
    object PantallaAgregar: Routes("Agregar")
    object PantallaEditar: Routes("Editar")
    object PantallaVender: Routes("Vender")
    object PantallaRegistro: Routes("Registro")
    object PantallaTiendas: Routes("Tiendas")
    object PantallaExportar: Routes("Exportar")



}


sealed class DrawerNavDestination(val route: String, @StringRes val title: Int, val contentDescription:String, val icon: ImageVector,)
{


     object Resumen : DrawerNavDestination(
        route = Routes.PantallaInicio.route,
        title =  R.string.menu_resumen,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Home,
    )

    data object Agregar : DrawerNavDestination(
        route = Routes.PantallaAgregar.route,
        title =  R.string.menu_agregar,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Add,
    )

    data object Editar : DrawerNavDestination(
        route = Routes.PantallaEditar.route,
        title =  R.string.menu_editar,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Edit,
    )

    data object Vender : DrawerNavDestination(
        route = Routes.PantallaVender.route,
        title =  R.string.menu_vender,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.PointOfSale,
    )

    data object Registro : DrawerNavDestination(
        route = Routes.PantallaRegistro.route,
        title =  R.string.menu_registro,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.ViewAgenda,
    )

    data object Tiendas : DrawerNavDestination(
        route = Routes.PantallaTiendas.route,
        title =  R.string.menu_tiendas,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Storefront,
    )

    data object Exportar : DrawerNavDestination(
        route = Routes.PantallaExportar.route,
        title =  R.string.menu_exportar,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.ImportExport,
    )





}