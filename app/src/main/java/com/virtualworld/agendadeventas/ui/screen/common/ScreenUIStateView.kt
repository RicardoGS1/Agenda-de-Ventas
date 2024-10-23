package com.virtualworld.agendadeventas.ui.screen.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.virtualworld.agendadeventas.R
import kotlinx.coroutines.launch


@Composable
fun ScreenUIStateViwe(
    uiMessengerState: ScreenUiState,
    modifier: Modifier,
    onChangerMessenger: (ScreenUiState) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
    )

    fun iniciarSnakbar(texto: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = texto, duration = SnackbarDuration.Short,

                )
        }
    }



    if (uiMessengerState != ScreenUiState.NEUTRAL) {

        if (uiMessengerState == ScreenUiState.LOADING) {
            Dialog(onDismissRequest = { }) {
                CircularProgressIndicator()
            }
        }

        if (uiMessengerState == ScreenUiState.ERROR) {

            AlertDialog(onDismissRequest = {
                onChangerMessenger(ScreenUiState.NEUTRAL)
            },
                confirmButton = { },
                dismissButton = {
                    TextButton(onClick = {
                        onChangerMessenger(ScreenUiState.NEUTRAL)
                    }) {
                        Text(text = "ok")
                    }
                },
                title = { Text(text = "Error") },
                text = { Text(text = stringResource(id = R.string.mensaje_error)) })

        }

        if (uiMessengerState == ScreenUiState.OK) {

            iniciarSnakbar(stringResource(id = R.string.mensaje_exitoso_export))

        }
    }
}
