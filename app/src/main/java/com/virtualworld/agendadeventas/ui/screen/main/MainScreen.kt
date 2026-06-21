package com.virtualworld.agendadeventas.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.virtualworld.agendadeventas.BuildConfig
import com.virtualworld.agendadeventas.R
import com.virtualword3d.salesregister.Navegation.AppNavegation
import com.virtualword3d.salesregister.Navegation.navigateToBottomNavDestination
import com.virtualworld.agendadeventas.navigation.DrawerMenuItem
import com.virtualworld.agendadeventas.navigation.DrawerNavDestination
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.main.view.MyTopAppBar
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    onLanguageChanged: () -> Unit = {},
    debugSeedViewModel: DebugSeedViewModel = hiltViewModel(),
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDebugBuild = BuildConfig.DEBUG
    val menuItems = DrawerNavDestination.getMenuItems(includeDebugItems = isDebugBuild)
    val selectedItem = DrawerNavDestination.fromRoute(currentRoute) ?: DrawerNavDestination.Resumen

    val snackbarHostState = remember { SnackbarHostState() }
    val seedState by debugSeedViewModel.seedState.collectAsStateWithLifecycle()
    val isSeeding by debugSeedViewModel.isLoading.collectAsStateWithLifecycle()

    val seedSuccessMessage = stringResource(R.string.debug_seed_data_success)
    val seedErrorMessage = stringResource(R.string.debug_seed_data_error)

    LaunchedEffect(seedState) {
        when (seedState) {
            ScreenUiState.OK -> {
                snackbarHostState.showSnackbar(seedSuccessMessage)
                debugSeedViewModel.clearSeedState()
            }
            ScreenUiState.ERROR -> {
                snackbarHostState.showSnackbar(seedErrorMessage)
                debugSeedViewModel.clearSeedState()
            }
            else -> Unit
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MyTopAppBar(
                title = stringResource(id = selectedItem.title),
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

                        menuItems.forEach { menuItem ->
                            when (menuItem) {
                                DrawerMenuItem.Separator -> {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                                DrawerMenuItem.DebugSeedTestData -> {
                                    NavigationDrawerItem(
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Default.Science,
                                                contentDescription = stringResource(R.string.debug_seed_data_cd),
                                            )
                                        },
                                        label = { Text(stringResource(R.string.debug_seed_data)) },
                                        selected = false,
                                        onClick = {
                                            if (isSeeding) return@NavigationDrawerItem
                                            scope.launch { drawerState.close() }
                                            debugSeedViewModel.seedTestData()
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                    )
                                }
                                is DrawerMenuItem.Destination -> {
                                    val item = menuItem.destination
                                    NavigationDrawerItem(
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.contentDescription
                                            )
                                        },
                                        label = { Text(stringResource(id = item.title)) },
                                        selected = item == selectedItem,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            navController.navigateToBottomNavDestination(item)
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            content = {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavegation(navController, contentPadding, onLanguageChanged)
                }


            })


    }


}

