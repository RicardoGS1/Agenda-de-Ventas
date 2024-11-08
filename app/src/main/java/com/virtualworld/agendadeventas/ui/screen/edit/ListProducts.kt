package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.domain.Model.ProductWithStoresActive

@Composable
fun ListProducts(
    changeWindowEditView: () -> Unit,
    productsState: List<ProductWithStoresActive>,
    changeProductSelect: (ProductWithStoresActive) -> Unit,
    onRemoveProduct: (ProductWithStoresActive) -> Unit,
) {


    LazyColumn {
        productsState.forEach { product ->

            item {
                ItemProduct(product, changeWindowEditView, changeProductSelect, onRemoveProduct)
            }

        }

    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun ItemProduct(
    product: ProductWithStoresActive,
    changeWindowEditView: () -> Unit,
    changeProductSelect: (ProductWithStoresActive) -> Unit,
    onRemoveProduct: (ProductWithStoresActive) -> Unit
) {

    var isDismissed by remember { mutableStateOf(false) }
    var confirmDeleted by remember {
        mutableStateOf(false)
    }
    val chengeIsDismissed = { isDismissed = false }
    val changeConfirmDelete = { confirmDeleted = true }

    if (isDismissed) {
        // El elemento ha sido eliminado, puedes eliminarlo de tu fuente de datos aquí
        println(isDismissed)

        ConfirmDelete(product, changeConfirmDelete, chengeIsDismissed)
        if (confirmDeleted == true) {
            onRemoveProduct(product)
            isDismissed = false
        }
        return // Ya no compongas el elemento
    }

    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                println("mrkgnmrkgnkrgnkjrng")
                isDismissed = true

            }
            true // Return trueto indicate that the dismiss action is confirmed
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart), // Permite deslizar de derecha a izquierda
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.2f else 0.5f)
        },
        background = {
            val color by animateColorAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) Color.Transparent else Color.Red,
                animationSpec = tween( // Use tween for smooth animation
                    durationMillis = 200, // Adjust duration as needed
                    easing = LinearEasing // Adjust easing function as needed
                ),
                label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White
                )
            }
        },
        dismissContent = {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .clickable {
                        changeWindowEditView()
                        changeProductSelect(product)
                    }
            ) {

                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(4.dp)
                ) {

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 4.dp),
                        text = product.productName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    FlowRow(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(32.dp) // Agrega espacio entre elementos
                    ) {
                        Text(text = stringResource(id = R.string.editar_costo) + " " + product.productCost)

                        product.storesValues.forEach {
                            Text(text = it.nameStore + ": " + it.value)
                        }
                    }
                }
            }
        }

    )
}

@Composable
fun ConfirmDelete(
    product: ProductWithStoresActive,
    changeConfirmDelete: () -> Unit,
    chengeIsDismissed: () -> Unit
) {


    AlertDialog(
        onDismissRequest = {
            chengeIsDismissed()
        },
        confirmButton = {
            TextButton(onClick = {
                changeConfirmDelete()
            }
            ) {
                Text(
                    text = stringResource(id = R.string.boton_confirmar_borrar),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                chengeIsDismissed()
            }
            ) {
                Text(
                    text = stringResource(id = R.string.boton_confirmar_no_borrar),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = { Text(text = stringResource(id = R.string.ventana_borrar)) },
        text = { Text(text = product.productName) }
    )


}