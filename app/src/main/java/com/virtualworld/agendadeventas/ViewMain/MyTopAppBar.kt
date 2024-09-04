package com.virtualword3d.salesregister.ViewMain
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu


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
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { onClickDrawer() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        },

        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White

    )
}