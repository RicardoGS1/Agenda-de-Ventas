package com.virtualworld.agendadeventas.ui.screen.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.domain.Model.ProductWithStoresActive

@Composable
fun WindowsEditView(
    changeWindowEditView: () -> Unit,
    productSelectState: ProductWithStoresActive,
    updateProductName: (String) -> Unit,
    updateProductCost: (String) -> Unit,
    updateStoreValue: (String, String) -> Unit,
    setProduct: (ProductWithStoresActive) -> Unit
) {



    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        // backgroundColor = MaterialTheme.colors.surface,
        elevation = CardDefaults.cardElevation(10.dp),
    ) {

        Box(Modifier.fillMaxSize()) {


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //NOMBRE PRODUCTO
                item {
                    TextField(
                        value = productSelectState.productName,
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
                        value = productSelectState.productCost,
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
                productSelectState.storesValues.forEach { store ->
                    val storeValue = store.value


                    item {
                        TextField(
                            value = storeValue,
                            onValueChange = {updateStoreValue(store.idStore,it)
                            },
                            label = { Text(text = stringResource(id = R.string.label_precio_venta) + " ${store.nameStore}") },
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }

            }








            Button(
                onClick = { changeWindowEditView() },
                Modifier.align(Alignment.BottomStart).padding(8.dp)
            ) {
                Text(text = "Cancel")

            }

            Button(
                onClick = { setProduct(productSelectState) },
                Modifier.align(Alignment.BottomEnd).padding(8.dp)
            ) {
                Text(text = "Salve")

            }
        }
    }


}