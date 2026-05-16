package com.virtualworld.agendadeventas.ui.screen.sell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.model.ProductStoreCore
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.common.StoreSelectView
import java.text.DecimalFormat

@Composable
fun SellScreen(viewModel: SellViewModel = hiltViewModel()) {

    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val productForStore by viewModel.productForStore.collectAsState()
    val listChangerProductsSell by viewModel.listChangerProductsSell.collectAsState()
    val uiMessengerState by viewModel.screenUiState.collectAsState()

    SellScreenContent(
        storesActiveState,
        productForStore,
        listChangerProductsSell,
        uiMessengerState,
        onChangerMessenger = { stateUi -> viewModel.changerUiState(stateUi) },
        onStoreSelected = { storeId -> viewModel.getProductForStore(storeId) },
        onUnitSellChanged = { changer -> viewModel.changerUnitSell(changer) },
        onSaveSellClicked = { storeId -> viewModel.salveSell(storeId) },
    )
}

@Composable
fun SellScreenContent(
    storesActiveState: List<Pair<Int, String>>,
    productForStore: List<ProductStoreCore>,
    listChangerProductsSell: List<Pair<Int, Int>>,
    uiMessengerState: ScreenUiState,
    onChangerMessenger: (ScreenUiState) -> Unit,
    onStoreSelected: (Int) -> Unit,
    onUnitSellChanged: (Pair<Int, Int>) -> Unit,
    onSaveSellClicked: (Int) -> Unit,
) {
    var selectedStoreIndex by remember { mutableStateOf(-1) }

    val sellTotalAmount = remember(productForStore, listChangerProductsSell) {
        val qtyByProductId = listChangerProductsSell.associate { it.first to it.second }
        productForStore.asSequence()
            .filter { it.idProduct != 0 && it.nombre.isNotBlank() }
            .sumOf { product ->
                val qty = qtyByProductId[product.idProduct] ?: 0
                (product.venta * qty).toDouble()
            }
            .toFloat()
    }

    LaunchedEffect(selectedStoreIndex) {
        if (selectedStoreIndex != -1) {
            onStoreSelected(storesActiveState[selectedStoreIndex].first)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                StoreSelectView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    label = stringResource(R.string.sell_label_tienda),
                    stores = storesActiveState,
                    selectedIndex = selectedStoreIndex,
                    isLoading = uiMessengerState == ScreenUiState.LOADING,
                    placeholder = stringResource(R.string.label_spiner_vender),
                    onStoreSelected = { index -> selectedStoreIndex = index },
                )
            }

            AnimatedVisibility(
                visible = selectedStoreIndex != -1 && sellTotalAmount > 0f,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                SellTotalBanner(amountFormatted = sellTotalPriceFormat.format(sellTotalAmount))
            }

            if (selectedStoreIndex != -1) {
                ListProductSellView(
                    products = productForStore,
                    changeUnitSell = onUnitSellChanged,
                    listChangerProductsSell = listChangerProductsSell,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        SaveButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            enabled = selectedStoreIndex != -1,
            onClick = { onSaveSellClicked(storesActiveState[selectedStoreIndex].first) },
        )

        ManagerScreenStateView(
            uiMessengerState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChangerMessenger = onChangerMessenger,
        )
    }
}

private val sellTotalPriceFormat = DecimalFormat("#.##")

@Composable
private fun SellTotalBanner(amountFormatted: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.AttachMoney,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(26.dp),
            )
            Text(
                text = stringResource(id = R.string.sell_total_sale, amountFormatted),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun SaveButton(
    modifier: Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        onClick = onClick,
    ) {
        Text(text = stringResource(id = R.string.boton_guardar_vender))
    }
}
