package com.virtualworld.agendadeventas.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.ui.graphics.vector.ImageVector
import com.virtualworld.agendadeventas.R

sealed interface DrawerMenuItem {
    data class Destination(val destination: DrawerNavDestination) : DrawerMenuItem
    data object Separator : DrawerMenuItem
    data object DebugSeedTestData : DrawerMenuItem
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

    object RegistroCompras : DrawerNavDestination(
        route = "RegistroCompras",
        title = R.string.menu_registro_compras,
        contentDescription = "go to screen Registro Compras",
        icon = Icons.Default.Assignment,
    )

    object Tiendas : DrawerNavDestination(
        route = "Tiendas",
        title =  R.string.menu_tiendas,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.Storefront,
    )

    object Inventario : DrawerNavDestination(
        route = "Inventario",
        title = R.string.menu_inventario,
        contentDescription = "go to screen Inventario",
        icon = Icons.Default.Inventory2,
    )

    object Compras : DrawerNavDestination(
        route = "Compras",
        title = R.string.menu_compras,
        contentDescription = "go to screen Compras",
        icon = Icons.Default.ShoppingCart,
    )

    object Dinero : DrawerNavDestination(
        route = "Dinero",
        title = R.string.menu_dinero,
        contentDescription = "go to screen Dinero",
        icon = Icons.Default.AccountBalanceWallet,
    )

    object AnalisisIA : DrawerNavDestination(
        route = "AnalisisIA",
        title = R.string.menu_analisis_ia,
        contentDescription = "go to screen AI Analysis",
        icon = Icons.Default.AutoAwesome,
    )

    object Exportar : DrawerNavDestination(
        route = "Exportar",
        title =  R.string.menu_exportar,
        contentDescription = "go to screen Resumen",
        icon = Icons.Default.ImportExport,
    )

    object Configuracion : DrawerNavDestination(
        route = "Configuracion",
        title = R.string.menu_configuracion,
        contentDescription = "go to screen Settings",
        icon = Icons.Default.Settings,
    )

    companion object {
        private val allDestinations: List<DrawerNavDestination> = listOf(
            Resumen,
            Agregar,
            Editar,
            Vender,
            Registro,
            RegistroCompras,
            Tiendas,
            Inventario,
            Compras,
            Dinero,
            AnalisisIA,
            Exportar,
            Configuracion,
        )

        fun getMenuItems(includeDebugItems: Boolean = false): List<DrawerMenuItem> = buildList {
            add(DrawerMenuItem.Destination(Resumen))
            add(DrawerMenuItem.Destination(AnalisisIA))
            add(DrawerMenuItem.Separator)
            add(DrawerMenuItem.Destination(Dinero))
            add(DrawerMenuItem.Separator)
            add(DrawerMenuItem.Destination(Agregar))
            add(DrawerMenuItem.Destination(Editar))
            add(DrawerMenuItem.Separator)
            add(DrawerMenuItem.Destination(Vender))
            add(DrawerMenuItem.Destination(Compras))
            add(DrawerMenuItem.Separator)
            add(DrawerMenuItem.Destination(Registro))
            add(DrawerMenuItem.Destination(RegistroCompras))
            add(DrawerMenuItem.Destination(Inventario))
            add(DrawerMenuItem.Separator)
            add(DrawerMenuItem.Destination(Tiendas))
            add(DrawerMenuItem.Destination(Configuracion))
            if (includeDebugItems) {
                add(DrawerMenuItem.Separator)
                add(DrawerMenuItem.DebugSeedTestData)
            }
        }

        fun fromRoute(route: String?): DrawerNavDestination? {
            if (route == null) return null
            return allDestinations.find { it.route == route }
                ?: if (route.startsWith(Configuracion.route)) Configuracion else null
        }
    }

}

