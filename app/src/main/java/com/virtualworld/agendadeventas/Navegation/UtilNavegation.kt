package com.virtualword3d.salesregister.Navegation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.virtualworld.agendadeventas.Navegation.DrawerNavDestination

fun NavController.navigateToBottomNavDestination(item: DrawerNavDestination) {
    navigate(item.route) {
        popUpTo(graph.findStartDestination().id) {
            this.saveState = true
        }
        restoreState = true
        launchSingleTop = true
    }
}