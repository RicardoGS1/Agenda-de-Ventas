package com.virtualworld.agendadeventas.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.virtualworld.agendadeventas.ui.Screen.MainScreen.MainScreen
import com.virtualworld.agendadeventas.ui.theme.AgendaDeVentasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val changeColorBarNotification: (androidx.compose.ui.graphics.Color) -> Unit = { color ->
            window.statusBarColor = color.toArgb()
        }
        super.onCreate(savedInstanceState)
        setContent {

            AgendaDeVentasTheme {
                MainScreen(changeColorBarNotification)
            }
        }
    }
}



