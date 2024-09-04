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
import com.virtualword3d.salesregister.Screen.Exportar.PantallaExportar
import com.virtualword3d.salesregister.Screen.Inicio.PantallaInicio
import com.virtualword3d.salesregister.Screen.Registro.PantallaRegistro
import com.virtualword3d.salesregister.Screen.Tiendas.PantallaTiendas
import com.virtualword3d.salesregister.Screen.Venta.PantallaVender
import com.virtualworld.agendadeventas.Navegation.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavegation(navController: NavHostController, paddingValues: PaddingValues = PaddingValues()){

    NavHost(navController = navController, startDestination = Routes.PantallaInicio.route){
        composable(Routes.PantallaInicio.route) { PantallaInicio() }
        composable(Routes.PantallaAgregar.route) { PantallaAgregar() }
        composable(Routes.PantallaEditar.route) { PantallaEditar() }
        composable(Routes.PantallaVender.route) { PantallaVender() }
        composable(Routes.PantallaRegistro.route) { PantallaRegistro() }
        composable(Routes.PantallaTiendas.route) { PantallaTiendas() }
        composable(Routes.PantallaExportar.route) { PantallaExportar() }

    }
}