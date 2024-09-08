package com.virtualworld.agendadeventas.ViewMain

import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun MyTopAppBar(
    title: String,
    modifier: Modifier,
    onClickDrawer: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title,color=MaterialTheme.colorScheme.primaryContainer) },
        navigationIcon = {
            IconButton(onClick = { onClickDrawer() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.primaryContainer)
            }
        },

        backgroundColor = MaterialTheme.colorScheme.primary,
       // contentColor = MaterialTheme.colorScheme.primaryContainer

    )
}