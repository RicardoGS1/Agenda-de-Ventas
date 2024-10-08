package com.virtualword3d.salesregister.Navegation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.virtualword3d.salesregister.Screen.Agregar.PantallaAgregar
import com.virtualword3d.salesregister.Screen.Editar.PantallaEditar
import com.virtualworld.agendadeventas.ui.Screen.Inicio.PantallaInicio
import com.virtualworld.agendadeventas.ui.Screen.record.RecordScreen
import com.virtualword3d.salesregister.Screen.Tiendas.PantallaTiendas
import com.virtualword3d.salesregister.Screen.Venta.SellScreen
import com.virtualworld.agendadeventas.navigation.DrawerNavDestination

//import com.virtualworld.agendadeventas.Navegation.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavegation(navController: NavHostController, paddingValues: PaddingValues = PaddingValues()){

    NavHost(navController = navController, startDestination = DrawerNavDestination.Resumen.route){
        composable(DrawerNavDestination.Resumen.route) { PantallaInicio() }
        composable(DrawerNavDestination.Agregar.route) { PantallaAgregar() }
        composable(DrawerNavDestination.Editar.route) { PantallaEditar() }
       composable(DrawerNavDestination.Vender.route) { SellScreen() }
       composable(DrawerNavDestination.Registro.route) { RecordScreen() }
        composable(DrawerNavDestination.Tiendas.route) { PantallaTiendas() }
       // composable(DrawerNavDestination.Resumen.route) { PantallaExportar() }

    }
}