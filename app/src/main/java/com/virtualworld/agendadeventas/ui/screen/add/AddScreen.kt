package com.virtualworld.agendadeventas.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView


import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState


@Composable
fun AddScreen() {

    val viewModel: AddViewModel = hiltViewModel()
    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val productUiState by viewModel.productUiState.collectAsState()
    val screenUiState: ScreenUiState by viewModel.screenUiState.collectAsState()

    val onChangerScreenUiState = { stateUi: ScreenUiState -> viewModel.changerUiState(stateUi) }
    val updateProductName = { name: String -> viewModel.updateProductName(name) }
    val updateProductCost = { cost: String -> viewModel.updateProductCost(cost) }
    val updateStoreValue = { storeId: Int, value: String ->
        viewModel.updateStoreValue(
            storeId,
            value
        )
    }
    val setProduct = { viewModel.setProduct() }


    ContentAddProduct(
        productUiState,
        storesActiveState,
        screenUiState,
        onChangerScreenUiState, updateProductName, updateProductCost, updateStoreValue, setProduct
    )


}


@Composable
fun ContentAddProduct(
    productUiState: ProductUiState,
    storesActiveState: List<Pair<Int, String>>,
    screenUiState: ScreenUiState,
    onChangerScreenUiState: (ScreenUiState) -> Unit,
    updateProductName: (String) -> Unit,
    updateProductCost: (String) -> Unit,
    updateStoreValue: (Int, String) -> Unit,
    setProduct: () -> Unit,
) {

    Box() {

        Column {
            //TITULO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(id = R.string.titulo_agregar),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //NOMBRE PRODUCTO
                item {
                    TextField(
                        value = productUiState.productName,
                        onValueChange = { updateProductName(it) },
                        label = { Text(text = stringResource(id = R.string.label_nombre_producto)) },
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true
                    )
                }

                //PRECIO COMPRA
                item {
                    TextField(
                        value = productUiState.productCost,
                        onValueChange = { updateProductCost(it) },
                        label = { Text(text = stringResource(id = R.string.label_precio_compra)) },
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }

                //STORES
                storesActiveState.forEach { store ->
                    val storeValue = productUiState.storeValues[store.first] ?: ""


                    item {
                        TextField(
                            value = storeValue,
                            onValueChange = { newValue ->
                                updateStoreValue(store.first, newValue)
                            },
                            label = { Text(text = stringResource(id = R.string.label_precio_venta) + " ${store.second}") },
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }


            }
        }

        Button(
            onClick = { setProduct() },
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),


            ) {
            Text(
                text = stringResource(id = R.string.boton_guardar_agregar),
                color = MaterialTheme.colorScheme.primaryContainer
            )
        }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChangerScreenUiState
        )
    }

}













