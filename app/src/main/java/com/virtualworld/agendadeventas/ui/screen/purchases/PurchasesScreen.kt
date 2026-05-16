package com.virtualworld.agendadeventas.ui.screen.purchases

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import java.text.DecimalFormat
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.common.StoreSelectView

@Composable
fun PurchasesScreen(viewModel: PurchasesViewModel = hiltViewModel()) {
    val screenUiState by viewModel.screenUiState.collectAsState()
    val storesActiveState by viewModel.storesActiveState.collectAsState()
    val purchaseRows by viewModel.purchaseRows.collectAsState()
    val addAmounts by viewModel.addAmounts.collectAsState()

    PurchasesContent(
        screenUiState = screenUiState,
        storesActiveState = storesActiveState,
        purchaseRows = purchaseRows,
        addAmounts = addAmounts,
        onIncrement = viewModel::incrementAmount,
        onDecrement = viewModel::decrementAmount,
        onConfirm = viewModel::confirmPurchases,
        onChangerUiState = viewModel::changerUiState,
        onStoreSelected = viewModel::selectStore,
    )
}

@Composable
private fun PurchasesContent(
    screenUiState: ScreenUiState,
    storesActiveState: List<Pair<Int, String>>,
    purchaseRows: List<PurchaseProductRow>,
    addAmounts: Map<Long, Int>,
    onIncrement: (Long) -> Unit,
    onDecrement: (Long) -> Unit,
    onConfirm: () -> Unit,
    onChangerUiState: (ScreenUiState) -> Unit,
    onStoreSelected: (Int?) -> Unit,
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

    val purchaseTotalAmount = remember(purchaseRows, addAmounts) {
        purchaseRows.sumOf { row ->
            val qty = addAmounts[row.id] ?: 0
            (row.compra * qty).toDouble()
        }.toFloat()
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
                    label = stringResource(R.string.purchases_store_label),
                    stores = storesActiveState,
                    selectedIndex = selectedStoreIndex,
                    isLoading = screenUiState == ScreenUiState.LOADING && storesActiveState.isEmpty(),
                    placeholder = stringResource(R.string.label_spiner_vender),
                    onStoreSelected = { index -> selectedStoreIndex = index },
                )
            }

            AnimatedVisibility(
                visible = screenUiState != ScreenUiState.LOADING &&
                    purchaseRows.isNotEmpty() &&
                    purchaseTotalAmount > 0f,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                PurchasesTotalBanner(
                    amountFormatted = purchasesTotalPriceFormat.format(purchaseTotalAmount),
                )
            }

            when {
                storesActiveState.isEmpty() && screenUiState != ScreenUiState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.sell_no_stores),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                selectedStoreIndex == -1 -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.purchases_pick_store),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                screenUiState == ScreenUiState.LOADING && purchaseRows.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                purchaseRows.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        PurchasesEmptyState()
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
                            bottom = 88.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(items = purchaseRows, key = { it.id }) { row ->
                            PurchaseProductItem(
                                row = row,
                                amount = addAmounts[row.id] ?: 0,
                                onIncrement = { onIncrement(row.id) },
                                onDecrement = { onDecrement(row.id) },
                            )
                        }
                    }
                }
            }
        }

        if (screenUiState != ScreenUiState.LOADING &&
            selectedStoreIndex != -1 &&
            purchaseRows.isNotEmpty()
        ) {
            val hasAnyAmount = addAmounts.values.any { it > 0 }
            Button(
                onClick = onConfirm,
                enabled = hasAnyAmount,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp),
                )
                Text(
                    text = stringResource(id = R.string.purchases_confirm_button),
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }

        ManagerScreenStateView(
            uiMessengerState = screenUiState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChangerMessenger = onChangerUiState,
        )
    }
}

private val purchasesTotalPriceFormat = DecimalFormat("#.##")

@Composable
private fun PurchasesTotalBanner(amountFormatted: String) {
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
                text = stringResource(id = R.string.purchases_total, amountFormatted),
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun PurchaseProductItem(
    row: PurchaseProductRow,
    amount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = row.nombre.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

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
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
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
                    if (amount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.65f),
                        ) {
                            Text(
                                text = stringResource(R.string.purchases_adding_amount, amount),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                FilledIconButton(
                    onClick = onDecrement,
                    enabled = amount > 0,
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }

                Text(
                    text = amount.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (amount > 0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    minLines = 1,
                )

                FilledIconButton(
                    onClick = onIncrement,
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PurchasesEmptyState() {
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
