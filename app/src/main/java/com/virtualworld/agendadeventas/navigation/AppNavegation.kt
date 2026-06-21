package com.virtualword3d.salesregister.Navegation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.virtualworld.agendadeventas.ui.screen.add.AddScreen
import com.virtualworld.agendadeventas.ui.screen.edit.ScreenEdit
import com.virtualworld.agendadeventas.ui.screen.resume.ResumeScreen
import com.virtualworld.agendadeventas.ui.screen.record.PurchaseRecordScreen
import com.virtualworld.agendadeventas.ui.screen.record.RecordScreen
import com.virtualworld.agendadeventas.ui.screen.stores.ScreenStores
import com.virtualworld.agendadeventas.ui.screen.sell.SellScreen
import com.virtualworld.agendadeventas.navigation.DrawerNavDestination
import com.virtualword3d.salesregister.Navegation.navigateToBottomNavDestination
import com.virtualworld.agendadeventas.ui.screen.ai.AiAnalysisScreen
import com.virtualworld.agendadeventas.ui.screen.export.ExportScreen
import com.virtualworld.agendadeventas.navigation.SettingsRoutes
import com.virtualworld.agendadeventas.ui.screen.inventory.InventoryScreen
import com.virtualworld.agendadeventas.ui.screen.money.MoneyScreen
import com.virtualworld.agendadeventas.ui.screen.purchases.PurchasesScreen
import com.virtualworld.agendadeventas.ui.screen.settings.LanguageSettingsScreen
import com.virtualworld.agendadeventas.ui.screen.settings.SettingsScreen

//import com.virtualworld.agendadeventas.Navegation.Routes

@Composable
fun AppNavegation(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues(),
    onLanguageChanged: () -> Unit = {}
) {

    NavHost(navController = navController, startDestination = DrawerNavDestination.Resumen.route){
        composable(DrawerNavDestination.Resumen.route) {
            ResumeScreen(
                onNavigateToStores = {
                    navController.navigateToBottomNavDestination(DrawerNavDestination.Tiendas)
                },
                onNavigateToMoney = {
                    navController.navigateToBottomNavDestination(DrawerNavDestination.Dinero)
                },
            )
        }
        composable(DrawerNavDestination.Agregar.route) { AddScreen() }
        composable(DrawerNavDestination.Editar.route) { ScreenEdit() }
       composable(DrawerNavDestination.Vender.route) { SellScreen() }
       composable(DrawerNavDestination.Registro.route) { RecordScreen() }
       composable(DrawerNavDestination.RegistroCompras.route) { PurchaseRecordScreen() }
        composable(DrawerNavDestination.Tiendas.route) {
            ScreenStores(
                onNavigateToAddProduct = {
                    navController.navigateToBottomNavDestination(DrawerNavDestination.Agregar)
                }
            )
        }
        composable(DrawerNavDestination.Inventario.route) { InventoryScreen() }
        composable(DrawerNavDestination.Compras.route) { PurchasesScreen() }
        composable(DrawerNavDestination.Dinero.route) { MoneyScreen() }
        composable(DrawerNavDestination.AnalisisIA.route) { AiAnalysisScreen() }
        composable(DrawerNavDestination.Exportar.route) { ExportScreen() }
        composable(DrawerNavDestination.Configuracion.route) {
            SettingsScreen(
                onNavigateToLanguage = {
                    navController.navigate(SettingsRoutes.LANGUAGE)
                }
            )
        }
        composable(SettingsRoutes.LANGUAGE) {
            LanguageSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLanguageChanged = onLanguageChanged
            )
        }

    }
}