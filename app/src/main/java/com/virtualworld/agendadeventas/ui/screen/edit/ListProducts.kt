package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.compose.animation.animateContentSize
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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive


@Composable
fun ListProductsSwipeToDismiss(
    productsState: List<ProductWithStoresActive>,
    selectProduct: (ProductWithStoresActive) -> Unit,
    changeShowEditView: () -> Unit,

    showConfirmDelete: Boolean,
    changeShowDelete: (Boolean) -> Unit
) {

  var cancelDelete by remember { mutableStateOf (false) }

    LaunchedEffect(key1 = showConfirmDelete) {
        if(!showConfirmDelete){
            cancelDelete=!cancelDelete
        }
    }


    LazyColumn() {

        items(productsState.size) { product ->

            SwipeBox(
                onDelete = {

                    selectProduct(productsState[product])
                    changeShowDelete(true)
                },
                onEdit = {
                },

                modifier = Modifier,
                restart =  cancelDelete
            ) {

                ListItem(productsState[product], selectProduct, changeShowEditView,)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListItem(
    product: ProductWithStoresActive,
    changeProductSelect: (ProductWithStoresActive) -> Unit,
    changeWindowEditView: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical =  2.dp).background(MaterialTheme.colorScheme.inverseOnSurface)
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



@Composable
private fun SwipeBox(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    restart : Boolean,
    content: @Composable () -> Unit,

) {

    val swipeState = rememberSwipeToDismissBoxState()

    LaunchedEffect(key1 = restart) {
        swipeState.reset()
    }

    lateinit var icon: ImageVector
    lateinit var alignment: Alignment
    val color: Color

    when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.error
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            icon = Icons.Outlined.Edit
            alignment = Alignment.CenterStart
            color =
                Color.Green.copy(alpha = 0.3f)
        }

        SwipeToDismissBoxValue.Settled -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.surface
        }
    }

    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        enableDismissFromStartToEnd = false,
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxSize()
                    .background(color)
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    imageVector = icon, contentDescription = null
                )
            }
        }
    ) {
        content()
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(swipeState) {
                onDelete()
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(swipeState) {
                onEdit()
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.Settled -> {
        }
    }
}
