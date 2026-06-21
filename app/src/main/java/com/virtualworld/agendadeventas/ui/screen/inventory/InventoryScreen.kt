package com.virtualworld.agendadeventas.ui.screen.inventory

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ProductAvatar
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.common.StoreSelectView

@Composable
fun InventoryScreen(viewModel: InventoryViewModel = hiltViewModel()) {
    val screenUiState by viewModel.screenUiState.collectAsState()
    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val inventoryRows by viewModel.inventoryRows.collectAsState()
    val historyTarget by viewModel.historyTarget.collectAsState()
    val purchaseHistory by viewModel.purchaseHistory.collectAsState()
    val historyLoading by viewModel.historyLoading.collectAsState()

    InventoryContent(
        screenUiState = screenUiState,
        storesActiveState = storesActiveState,
        inventoryRows = inventoryRows,
        onChangerUiState = viewModel::changerUiState,
        onStoreSelected = viewModel::selectStore,
        onOpenHistory = viewModel::openPurchaseHistory,
    )

    historyTarget?.let { target ->
        PurchaseHistoryDialog(
            productName = target.productName,
            purchases = purchaseHistory,
            isLoading = historyLoading,
            onDismiss = viewModel::closePurchaseHistory,
        )
    }
}

@Composable
private fun InventoryContent(
    screenUiState: ScreenUiState,
    storesActiveState: List<Pair<Int, String>>,
    inventoryRows: List<InventoryProductRow>,
    onChangerUiState: (ScreenUiState) -> Unit,
    onStoreSelected: (Int?) -> Unit,
    onOpenHistory: (Long, String) -> Unit,
) {
    var selectedStoreIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(storesActiveState) {
        if (selectedStoreIndex >= storesActiveState.size) {
            selectedStoreIndex = -1
            onStoreSelected(null)
        }
    }

    LaunchedEffect(selectedStoreIndex, storesActiveState) {
        if (selectedStoreIndex >= 0 && selectedStoreIndex < storesActiveState.size) {
            onStoreSelected(storesActiveState[selectedStoreIndex].first)
        } else {
            onStoreSelected(null)
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
                    label = stringResource(R.string.inventory_store_label),
                    stores = storesActiveState,
                    selectedIndex = selectedStoreIndex,
                    isLoading = screenUiState == ScreenUiState.LOADING && storesActiveState.isEmpty(),
                    placeholder = stringResource(R.string.label_spiner_vender),
                    onStoreSelected = { index -> selectedStoreIndex = index },
                )
            }

            when {
                storesActiveState.isEmpty() && screenUiState != ScreenUiState.LOADING -> {
                    InventoryMessageBox(
                        text = stringResource(R.string.sell_no_stores),
                    )
                }

                selectedStoreIndex == -1 -> {
                    InventoryMessageBox(
                        text = stringResource(R.string.inventory_pick_store),
                    )
                }

                screenUiState == ScreenUiState.LOADING && inventoryRows.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                inventoryRows.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        InventoryEmptyState()
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 72.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(items = inventoryRows, key = { it.id }) { row ->
                            InventoryProductItem(
                                row = row,
                                onOpenHistory = { onOpenHistory(row.id, row.nombre) },
                            )
                        }
                    }
                }
            }
        }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChangerMessenger = onChangerUiState,
        )
    }
}

@Composable
private fun InventoryMessageBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun InventoryProductItem(
    row: InventoryProductRow,
    onOpenHistory: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductAvatar(
                name = row.nombre,
                imagePath = row.imagePath,
                size = 48.dp,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
            ) {
                Text(
                    text = row.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Surface(
                    modifier = Modifier.padding(top = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                ) {
                    Text(
                        text = stringResource(R.string.purchases_current_stock, row.stockAtStore),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }

            IconButton(onClick = onOpenHistory) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = stringResource(R.string.inventory_view_history),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun InventoryEmptyState() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )
            }
            Text(
                text = stringResource(id = R.string.purchases_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(id = R.string.purchases_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
