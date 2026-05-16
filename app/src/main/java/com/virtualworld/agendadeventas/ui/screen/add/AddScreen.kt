package com.virtualworld.agendadeventas.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ProductFormInfoBanner
import com.virtualworld.agendadeventas.ui.screen.common.ProductInfoFormCard
import com.virtualworld.agendadeventas.ui.screen.common.ProductInventoryFormCard
import com.virtualworld.agendadeventas.ui.screen.common.ProductSalePricesFormCard
import com.virtualworld.agendadeventas.ui.screen.common.ProductUiState
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.common.StoreInventoryField
import com.virtualworld.agendadeventas.ui.screen.common.StorePriceField

@Composable
fun AddScreen() {
    val viewModel: AddViewModel = hiltViewModel()
    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val productUiState by viewModel.productUiState.collectAsState()
    val screenUiState: ScreenUiState by viewModel.screenUiState.collectAsState()

    ContentAddProduct(
        productUiState = productUiState,
        storesActiveState = storesActiveState,
        screenUiState = screenUiState,
        onChangerScreenUiState = viewModel::changerUiState,
        updateProductName = viewModel::updateProductName,
        updateProductCost = viewModel::updateProductCost,
        updateStoreValue = viewModel::updateStoreValue,
        updateTieneInventario = viewModel::updateTieneInventario,
        updateStoreInventory = viewModel::updateStoreInventory,
        setProduct = viewModel::setProduct
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
    updateTieneInventario: (Boolean) -> Unit,
    updateStoreInventory: (Int, String) -> Unit,
    setProduct: () -> Unit,
) {
    val isFormVisible = screenUiState != ScreenUiState.LOADING

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AddProductHeader()

            if (isFormVisible) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 88.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { ProductFormInfoBanner(descriptionResId = R.string.add_info_description) }
                    item {
                        ProductInfoFormCard(
                            productName = productUiState.productName,
                            productCost = productUiState.productCost,
                            onNameChange = updateProductName,
                            onCostChange = updateProductCost
                        )
                    }
                    if (storesActiveState.isNotEmpty()) {
                        item {
                            ProductSalePricesFormCard(
                                stores = storesActiveState.map { (storeId, storeName) ->
                                    StorePriceField(
                                        storeName = storeName,
                                        value = productUiState.storeValues[storeId] ?: "",
                                        onValueChange = { updateStoreValue(storeId, it) }
                                    )
                                }
                            )
                        }
                    }
                    item {
                        ProductInventoryFormCard(
                            tieneInventario = productUiState.tieneInventario,
                            storeInventoryFields = storesActiveState.map { (storeId, storeName) ->
                                StoreInventoryField(
                                    storeName = storeName,
                                    value = productUiState.storeInventoryValues[storeId] ?: "",
                                    onValueChange = { updateStoreInventory(storeId, it) },
                                )
                            },
                            onTieneInventarioChange = updateTieneInventario,
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (isFormVisible) {
            Button(
                onClick = setProduct,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp)
                )
                Text(
                    text = stringResource(id = R.string.boton_guardar_agregar),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChangerMessenger = onChangerScreenUiState
        )
    }
}

@Composable
private fun AddProductHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = stringResource(id = R.string.titulo_agregar),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.add_subtitle),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
