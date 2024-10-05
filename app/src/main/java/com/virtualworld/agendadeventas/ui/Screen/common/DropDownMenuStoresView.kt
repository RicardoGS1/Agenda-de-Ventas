package com.virtualworld.agendadeventas.ui.Screen.common

import android.os.Messenger
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.virtualword3d.salesregister.Data.Entity.Mensajes
import com.virtualworld.agendadeventas.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun DropDownMenuStoresView(
    items: List<Pair<Int, String>>,
    uiMessengerState: Mensajes,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int) -> Unit,

    drawItem: @Composable (Pair<Int, String>, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->

        LargeDropdownMenuItem(
            text = item.second,
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {



    var expanded by remember { mutableStateOf(true) }

    Surface(
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    ) {

        Box(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            contentAlignment = Alignment.Center
        ) {

            Button(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxSize()) {

                Text(
                    text = if (selectedIndex != -1) items.get(selectedIndex).second else "Select a store",
                    fontSize = 24.sp
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }


        if (expanded) {

            Dialog(
                onDismissRequest = { expanded = selectedIndex == -1 },
            ) {


                Surface(
                    shape = RoundedCornerShape(12.dp),
                ) {
                    val listState = rememberLazyListState()
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }


                    println(uiMessengerState)

                    LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {


                        item {
                            LargeDropdownMenuItem(
                                text = stringResource(id =  R.string.label_spiner_vender),
                                selected = false,
                                enabled = false,
                                onClick = { },
                            )
                        }

                        if(uiMessengerState!=Mensajes.CARGANDO) {
                            itemsIndexed(items) { index, item ->
                                val selectedItem = index == selectedIndex
                                drawItem(
                                    item,
                                    selectedItem,
                                    true
                                ) {
                                    onItemSelected(index)
                                    expanded = false
                                }

                                if (index < items.lastIndex) {
                                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }else{
                            item {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }}
                        }




                    }
                }
            }
        }
    }
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

