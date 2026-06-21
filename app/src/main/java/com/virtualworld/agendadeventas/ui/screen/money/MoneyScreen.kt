package com.virtualworld.agendadeventas.ui.screen.money

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.model.CashOperationCore
import com.virtualworld.agendadeventas.core.model.CashOperationType
import com.virtualworld.agendadeventas.core.model.StoreCashSummaryCore
import com.virtualworld.agendadeventas.ui.screen.common.ManagerScreenStateView
import com.virtualworld.agendadeventas.ui.screen.common.ScreenUiState
import com.virtualworld.agendadeventas.ui.screen.common.StoreSelectView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MoneyScreen(viewModel: MoneyViewModel = hiltViewModel()) {
    val screenUiState by viewModel.screenUiState.collectAsState()
    val storeSummaries by viewModel.storeSummaries.collectAsState()
    val operations by viewModel.operations.collectAsState()
    val selectedStoreId by viewModel.selectedStoreId.collectAsState()
    val dialogMode by viewModel.dialogMode.collectAsState()
    val insufficientCashError by viewModel.insufficientCashError.collectAsState()

    MoneyContent(
        screenUiState = screenUiState,
        storeSummaries = storeSummaries,
        operations = operations,
        selectedStoreId = selectedStoreId,
        dialogMode = dialogMode,
        insufficientCashError = insufficientCashError,
        onStoreSelected = viewModel::selectStore,
        onOpenDeposit = viewModel::openDepositDialog,
        onOpenWithdraw = viewModel::openWithdrawDialog,
        onDismissDialog = viewModel::dismissDialog,
        onConfirmOperation = viewModel::confirmCashOperation,
        onChangerUiState = viewModel::changerUiState,
    )
}

@Composable
private fun MoneyContent(
    screenUiState: ScreenUiState,
    storeSummaries: List<StoreCashSummaryCore>,
    operations: List<CashOperationCore>,
    selectedStoreId: Int?,
    dialogMode: CashDialogMode,
    insufficientCashError: Boolean,
    onStoreSelected: (Int?) -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmOperation: (String, String) -> Unit,
    onChangerUiState: (ScreenUiState) -> Unit,
) {
    val storesForSelector = remember(storeSummaries) {
        storeSummaries.map { it.storeId to it.storeName }
    }
    var selectedStoreIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(storesForSelector) {
        if (selectedStoreIndex >= storesForSelector.size) {
            selectedStoreIndex = -1
            onStoreSelected(null)
        }
    }

    LaunchedEffect(selectedStoreIndex, storesForSelector) {
        if (selectedStoreIndex >= 0 && selectedStoreIndex < storesForSelector.size) {
            onStoreSelected(storesForSelector[selectedStoreIndex].first)
        } else {
            onStoreSelected(null)
        }
    }

    val selectedSummary = remember(selectedStoreId, storeSummaries) {
        storeSummaries.find { it.storeId == selectedStoreId }
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
                    label = stringResource(R.string.money_store_label),
                    stores = storesForSelector,
                    selectedIndex = selectedStoreIndex,
                    isLoading = screenUiState == ScreenUiState.LOADING && storeSummaries.isEmpty(),
                    placeholder = stringResource(R.string.label_spiner_vender),
                    onStoreSelected = { index -> selectedStoreIndex = index },
                )
            }

            when {
                storeSummaries.isEmpty() && screenUiState != ScreenUiState.LOADING -> {
                    MoneyMessageBox(text = stringResource(R.string.sell_no_stores))
                }

                selectedStoreIndex == -1 -> {
                    MoneyMessageBox(text = stringResource(R.string.money_pick_store))
                }

                selectedSummary == null && screenUiState == ScreenUiState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                selectedSummary != null -> {
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
                        item {
                            MoneyBalanceCard(summary = selectedSummary)
                        }

                        item {
                            MoneyActionButtons(
                                onOpenDeposit = onOpenDeposit,
                                onOpenWithdraw = onOpenWithdraw,
                            )
                        }

                        item {
                            MoneyBreakdownCard(summary = selectedSummary)
                        }

                        if (operations.isEmpty()) {
                            item {
                                MoneyEmptyOperationsState()
                            }
                        } else {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 4.dp, bottom = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = stringResource(R.string.money_operations_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.History,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(14.dp),
                                            )
                                            Text(
                                                text = pluralStringResource(
                                                    id = R.plurals.money_operations_count,
                                                    count = operations.size,
                                                    operations.size,
                                                ),
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(start = 6.dp),
                                            )
                                        }
                                    }
                                }
                            }

                            val sections = groupOperationsByDate(operations)
                            sections.forEach { section ->
                                item(key = "header_${section.dayKey}") {
                                    MoneyDateSeparator(
                                        dayKey = section.dayKey,
                                        count = section.operations.size,
                                    )
                                }
                                items(
                                    items = section.operations,
                                    key = { op -> "${op.type.name}_${op.id}" },
                                ) { operation ->
                                    MoneyOperationItem(operation = operation)
                                }
                            }
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

    if (dialogMode != CashDialogMode.NONE) {
        CashOperationDialog(
            mode = dialogMode,
            insufficientCashError = insufficientCashError,
            onDismiss = onDismissDialog,
            onConfirm = onConfirmOperation,
        )
    }
}

@Composable
private fun MoneyBalanceCard(summary: StoreCashSummaryCore) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp),
                )
            }
            Text(
                text = stringResource(R.string.money_current_balance),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatMoneyAmount(summary.balance),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = summary.storeName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MoneyActionButtons(
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(
            onClick = onOpenDeposit,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(18.dp),
            )
            Text(text = stringResource(R.string.money_add_cash))
        }

        OutlinedButton(
            onClick = onOpenWithdraw,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(18.dp),
            )
            Text(text = stringResource(R.string.money_withdraw_cash))
        }
    }
}

@Composable
private fun MoneyBreakdownCard(summary: StoreCashSummaryCore) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.money_breakdown_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            MoneyBreakdownRow(
                label = stringResource(R.string.money_from_sales),
                amount = summary.salesTotal,
                icon = Icons.Outlined.PointOfSale,
                positive = true,
            )
            MoneyBreakdownRow(
                label = stringResource(R.string.money_from_deposits),
                amount = summary.depositsTotal,
                icon = Icons.Outlined.ArrowDownward,
                positive = true,
            )
            MoneyBreakdownRow(
                label = stringResource(R.string.money_from_withdrawals),
                amount = summary.withdrawalsTotal,
                icon = Icons.Outlined.ArrowUpward,
                positive = false,
            )
        }
    }
}

@Composable
private fun MoneyBreakdownRow(
    label: String,
    amount: Float,
    icon: ImageVector,
    positive: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = if (positive) "+${formatMoneyAmount(amount)}" else "-${formatMoneyAmount(amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (positive) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
        )
    }
}

@Composable
private fun MoneyOperationItem(operation: CashOperationCore) {
    val (icon, typeLabel, containerColor, amountColor) = when (operation.type) {
        CashOperationType.SALE -> OperationStyle(
            icon = Icons.Outlined.PointOfSale,
            labelRes = R.string.money_operation_sale,
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            amountColor = MaterialTheme.colorScheme.primary,
        )
        CashOperationType.DEPOSIT -> OperationStyle(
            icon = Icons.Outlined.Savings,
            labelRes = R.string.money_operation_deposit,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
            amountColor = MaterialTheme.colorScheme.tertiary,
        )
        CashOperationType.WITHDRAWAL -> OperationStyle(
            icon = Icons.Outlined.ArrowUpward,
            labelRes = R.string.money_operation_withdrawal,
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f),
            amountColor = MaterialTheme.colorScheme.error,
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(containerColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = amountColor,
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = containerColor,
                ) {
                    Text(
                        text = stringResource(typeLabel),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = amountColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    )
                }
                Text(
                    text = operationDescription(operation),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp),
                )
                if (operation.units != null) {
                    Text(
                        text = stringResource(R.string.money_sale_units, operation.units),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }

            Text(
                text = formatSignedMoneyAmount(operation.signedAmount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor,
            )
        }
    }
}

@Composable
private fun operationDescription(operation: CashOperationCore): String {
    return when (operation.type) {
        CashOperationType.SALE -> operation.description
        CashOperationType.DEPOSIT -> operation.description.ifBlank {
            stringResource(R.string.money_default_deposit_note)
        }
        CashOperationType.WITHDRAWAL -> operation.description.ifBlank {
            stringResource(R.string.money_default_withdraw_note)
        }
    }
}

private data class OperationStyle(
    val icon: ImageVector,
    val labelRes: Int,
    val containerColor: androidx.compose.ui.graphics.Color,
    val amountColor: androidx.compose.ui.graphics.Color,
)

@Composable
private fun MoneyDateSeparator(dayKey: Long, count: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatMoneySectionDate(dayKey),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
            ) {
                Text(
                    text = pluralStringResource(
                        id = R.plurals.money_day_operations_count,
                        count = count,
                        count,
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f),
            thickness = 1.dp,
        )
    }
}

@Composable
private fun CashOperationDialog(
    mode: CashDialogMode,
    insufficientCashError: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    var amountText by remember(mode) { mutableStateOf("") }
    var noteText by remember(mode) { mutableStateOf("") }

    val title = when (mode) {
        CashDialogMode.DEPOSIT -> stringResource(R.string.money_dialog_deposit_title)
        CashDialogMode.WITHDRAW -> stringResource(R.string.money_dialog_withdraw_title)
        CashDialogMode.NONE -> ""
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text(stringResource(R.string.money_amount_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text(stringResource(R.string.money_note_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (insufficientCashError) {
                    Text(
                        text = stringResource(R.string.money_insufficient_balance),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = { onConfirm(amountText, noteText) }) {
                Text(stringResource(R.string.boton_mensaje_confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.boton_cancelar))
            }
        },
    )
}

@Composable
private fun MoneyMessageBox(text: String) {
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
private fun MoneyEmptyOperationsState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp),
            )
            Text(
                text = stringResource(R.string.money_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.money_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private data class MoneyDateSection(
    val dayKey: Long,
    val operations: List<CashOperationCore>,
)

private fun groupOperationsByDate(operations: List<CashOperationCore>): List<MoneyDateSection> {
    return operations
        .groupBy { startOfDay(it.fecha) }
        .entries
        .sortedByDescending { it.key }
        .map { (dayKey, ops) -> MoneyDateSection(dayKey, ops.sortedByDescending { it.fecha }) }
}

private fun startOfDay(timestamp: Long): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

@Composable
private fun formatMoneySectionDate(dayKey: Long): String {
    val todayKey = startOfDay(System.currentTimeMillis())
    val yesterdayKey = startOfDay(
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis,
    )
    return when (dayKey) {
        todayKey -> stringResource(R.string.record_date_today)
        yesterdayKey -> stringResource(R.string.record_date_yesterday)
        else -> moneyDateFormatter.format(Date(dayKey))
    }
}

private val moneyDateFormatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

private val moneyAmountFormat = DecimalFormat("#.##")

private fun formatMoneyAmount(value: Float): String = moneyAmountFormat.format(value)

private fun formatSignedMoneyAmount(value: Float): String {
    val formatted = formatMoneyAmount(kotlin.math.abs(value))
    return if (value >= 0f) "+$formatted" else "-$formatted"
}
