package com.virtualworld.agendadeventas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.theme.AgendaDeVentasTheme
import com.virtualword3d.salesregister.Navegation.AppNavegation
import com.virtualworld.agendadeventas.Navegation.DrawerNavDestination
import com.virtualword3d.salesregister.Navegation.navigateToBottomNavDestination
import com.virtualword3d.salesregister.ViewMain.MyTopAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        setContent {

            AgendaDeVentasTheme {
                MainScrens(rememberNavController())
            }
        }
    }
}



@Composable
fun MainScrens(rememberNavController: NavHostController) {

    //VARIABLES DE LA VISTA
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        DrawerNavDestination.Resumen,
        DrawerNavDestination.Agregar,
        DrawerNavDestination.Editar,
        DrawerNavDestination.Vender,
        DrawerNavDestination.Registro,
        DrawerNavDestination.Tiendas,
        DrawerNavDestination.Exportar,
    )
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
        },
    )
    { contentPadding ->

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
                                    rememberNavController.navigateToBottomNavDestination(item)
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
                    //NavegationHost
                    AppNavegation(rememberNavController, contentPadding)
                }


            }
        )


    }


}

