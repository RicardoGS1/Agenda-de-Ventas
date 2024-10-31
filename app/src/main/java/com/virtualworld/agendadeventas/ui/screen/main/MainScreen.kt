package com.virtualworld.agendadeventas.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.virtualword3d.salesregister.Navegation.AppNavegation
import com.virtualword3d.salesregister.Navegation.navigateToBottomNavDestination
import com.virtualworld.agendadeventas.navigation.DrawerNavDestination
import com.virtualworld.agendadeventas.ui.screen.main.view.MyTopAppBar
import kotlinx.coroutines.launch

@Composable
fun MainScreen(changeColorBarNotification: (Color) -> Unit) {



    val navController = rememberNavController()

    val colorBarNotification = MaterialTheme.colorScheme.primary
    LaunchedEffect(key1 = true) {
        changeColorBarNotification(colorBarNotification)
    }


    //VARIABLES DE LA VISTA
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = DrawerNavDestination.getDestinations()
    val selectedItem = remember { mutableStateOf(items[0]) }


    Scaffold(

        topBar = {
            MyTopAppBar(
                title = stringResource(id = selectedItem.value.title),
                modifier = Modifier.statusBarsPadding(),
            ) {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }


            }
        }

    ) { contentPadding ->

        ModalNavigationDrawer(

            modifier = Modifier.padding(contentPadding),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet() {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Spacer(Modifier.height(12.dp))

                        items.forEach { item ->
                            NavigationDrawerItem(

                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.contentDescription
                                    )
                                },
                                label = { Text(stringResource(id = item.title)) },
                                selected = item == selectedItem.value,
                                onClick = {

                                    scope.launch { drawerState.close() }
                                    navController.navigateToBottomNavDestination(item)
                                    selectedItem.value = item
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }
            },
            content = {

                Surface(
                    modifier = Modifier.fillMaxSize(),

                    ) {

                    AppNavegation(navController, contentPadding)
                }


            })


    }


}

