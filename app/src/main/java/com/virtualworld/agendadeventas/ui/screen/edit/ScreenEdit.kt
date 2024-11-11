package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R


import com.virtualworld.agendadeventas.domain.models.ProductWithStoresActive
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState


@Composable
fun ScreenEdit() {

    val viewModel: EditViewModel = hiltViewModel()

    val screenUiState by viewModel.screenUiState.collectAsState()
    val productsState by viewModel.productsState.collectAsState()
    val productSelectState by viewModel.productSelectState.collectAsState()

    val windowEditView = rememberSaveable { (mutableStateOf(false)) }

    var showConfirmDelete by remember {
        mutableStateOf(false)
    }

    val changeShowConfirmDelete = { state: Boolean -> showConfirmDelete = state }


    val changeScreenUiState = { stateUi: ScreenUiState ->
        viewModel.changerUiState(stateUi)
    }
    val selectProduct = { product: ProductWithStoresActive ->
        viewModel.changeProductSelect(product)
    }
    val changeShowEditView = {
        windowEditView.value = !windowEditView.value
    }
    val onRemoveProduct = { product: ProductWithStoresActive ->
        viewModel.deleteProduct(product)
    }
    val setProduct = { product: ProductWithStoresActive ->
        viewModel.setProduct()
    }

    val updateProductName = { name: String -> viewModel.updateProductName(name) }
    val updateProductCost = { cost: String -> viewModel.updateProductCost(cost) }
    val updateStoreValue = { storeId: String, value: String ->
        viewModel.updateStoreValue(storeId, value)
    }


    Box() {

        if (screenUiState != ScreenUiState.LOADING) {


            ListProductsSwipeToDismiss(
                productsState,
                selectProduct,
                changeShowEditView,
                showConfirmDelete,
                changeShowConfirmDelete
            )

            WindowsEditView(
                changeShowEditView,
                productSelectState,
                updateProductName,
                updateProductCost,
                updateStoreValue,
                setProduct,
                windowEditView.value
            )

            DialogDelete(
                productSelectState,
                showConfirmDelete,
                changeShowConfirmDelete,
                onRemoveProduct
            )


        }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            changeScreenUiState
        )
    }

}


@Composable
fun DialogDelete(
    productSelectState: ProductWithStoresActive,
    showConfirmDelete: Boolean,
    changeShowConfirmDelete: (Boolean) -> Unit,
    onRemoveProduct: (ProductWithStoresActive) -> Unit
) {


    if (showConfirmDelete)
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.1f))
                .clickable { changeShowConfirmDelete(false) }
        )

    AnimatedVisibility(
        visible = showConfirmDelete,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 500)
        )
    ) {


        Box(Modifier.fillMaxSize()) {


            Card(
                Modifier
                    .padding(32.dp)
                    .align(Alignment.Center),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp,
                ),
                shape = RoundedCornerShape(8.dp),
               // colors = CardDefaults.cardColors(  MaterialTheme.colorScheme.onPrimary)
            ) {


                    Column(Modifier.padding(16.dp)) {
                        Text(text = stringResource(id = R.string.ventana_borrar), fontSize = 24.sp)
                        Text(text = productSelectState.productName)

                        Row(Modifier.fillMaxWidth(),  horizontalArrangement =Arrangement.End ) {

                            TextButton( modifier =  Modifier.padding(horizontal = 32.dp), onClick = {
                                onRemoveProduct(productSelectState)
                                changeShowConfirmDelete(false)
                            }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.boton_confirmar_borrar),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            TextButton(onClick = {
                                changeShowConfirmDelete(false)
                                // actualize ()
                                //  isVisible = false

                            }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.boton_confirmar_no_borrar),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                        }
                    }
                }


        }
        // }
    }
}
