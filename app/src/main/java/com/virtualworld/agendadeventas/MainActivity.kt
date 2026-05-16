package com.virtualworld.agendadeventas

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.virtualworld.agendadeventas.core.locale.LocaleManager
import com.virtualworld.agendadeventas.ui.screen.main.MainScreen
import com.virtualworld.agendadeventas.ui.theme.AgendaDeVentasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleManager.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AgendaDeVentasTheme {
                val colorBarNotification = MaterialTheme.colorScheme.primary
                val view = LocalView.current
                val darkTheme = isSystemInDarkTheme()
                if (!view.isInEditMode) {
                    val window = (view.context as Activity).window
                    window.statusBarColor = colorBarNotification.toArgb()
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                        darkTheme
                }
                MainScreen(
                    onLanguageChanged = { recreate() }
                )
            }
        }
    }
}
