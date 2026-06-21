package com.virtualworld.agendadeventas.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.model.PurchaseForStoreCore
import com.virtualworld.agendadeventas.ui.screen.common.ProductAvatar
import com.virtualworld.agendadeventas.ui.theme.AgendaDeVentasTheme
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private data class PurchaseRecordDateSection(
    val dayKey: Long,
    val purchases: List<PurchaseForStoreCore>,
)

@Composable
fun ListProductPurchaseRecordView(
    purchases: List<PurchaseForStoreCore>,
    modifier: Modifier = Modifier,
) {
    val sections = remember(purchases) {
        groupPurchaseRecordsByDate(purchases)
    }
    val totalPurchases = remember(sections) {
        sections.sumOf { it.purchases.size }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 12.dp,
            bottom = 72.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (sections.isEmpty()) {
            item {
                PurchaseRecordEmptyState()
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
                        text = pluralStringResource(
                            id = R.plurals.purchase_record_count,
                            count = totalPurchases,
                            totalPurchases,
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
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
                                    id = R.plurals.record_days_count,
                                    count = sections.size,
                                    sections.size,
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

            sections.forEach { section ->
                item(key = "header_${section.dayKey}") {
                    PurchaseRecordDateSeparator(
                        dayKey = section.dayKey,
                        purchasesCount = section.purchases.size,
                    )
                }

                items(
                    items = section.purchases,
                    key = { purchase -> purchase.id },
                ) { purchase ->
                    PurchaseRecordListItem(purchase = purchase)
                }
            }
        }
    }
}

@Composable
private fun PurchaseRecordDateSeparator(
    dayKey: Long,
    purchasesCount: Int,
) {
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
                text = formatPurchaseRecordSectionDate(dayKey),
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
                        id = R.plurals.purchase_record_day_count,
                        count = purchasesCount,
                        purchasesCount,
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
private fun PurchaseRecordListItem(purchase: PurchaseForStoreCore) {
    val shape = RoundedCornerShape(16.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                shape = shape,
            ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProductAvatar(
                    name = purchase.nombre,
                    imagePath = purchase.imagePath,
                    size = 48.dp,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp),
                ) {
                    Text(
                        text = purchase.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        PurchaseRecordInfoChip(
                            label = stringResource(id = R.string.record_unidades) + " ",
                            value = purchase.unidades.toString(),
                        )
                        PurchaseRecordInfoChip(
                            label = stringResource(id = R.string.record_costo) + " ",
                            value = formatPurchaseRecordPrice(purchase.compra),
                            highlighted = true,
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
            ) {
                Text(
                    text = stringResource(
                        id = R.string.record_line_total,
                        purchase.unidades,
                        formatPurchaseRecordPrice(purchase.compra),
                        formatPurchaseRecordPrice(purchase.compra * purchase.unidades),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End,
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (purchase.deductedFromCash) {
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                },
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = null,
                        tint = if (purchase.deductedFromCash) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = if (purchase.deductedFromCash) {
                            stringResource(R.string.purchase_record_cash_deducted)
                        } else {
                            stringResource(R.string.purchase_record_cash_not_deducted)
                        },
                        modifier = Modifier.padding(start = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (purchase.deductedFromCash) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun PurchaseRecordInfoChip(
    label: String,
    value: String,
    highlighted: Boolean = false,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (highlighted) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (highlighted) {
                Icon(
                    imageVector = Icons.Outlined.AttachMoney,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp),
                )
            }
            Text(
                text = label + value,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Medium,
                color = if (highlighted) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PurchaseRecordEmptyState() {
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
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )
            }
            Text(
                text = stringResource(id = R.string.purchase_record_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(id = R.string.purchase_record_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun groupPurchaseRecordsByDate(records: List<PurchaseForStoreCore>): List<PurchaseRecordDateSection> {
    return records
        .filter { it.id != 0L || it.nombre.isNotBlank() }
        .groupBy { purchaseDayKey(it.fecha) }
        .map { (key, purchases) ->
            PurchaseRecordDateSection(
                dayKey = key,
                purchases = purchases.sortedByDescending { it.fecha },
            )
        }
        .sortedByDescending { it.dayKey }
}

private fun purchaseDayKey(timestamp: Long): Long {
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
private fun formatPurchaseRecordSectionDate(dayKey: Long): String {
    val todayKey = purchaseDayKey(System.currentTimeMillis())
    val yesterdayKey = purchaseDayKey(
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis,
    )

    return when (dayKey) {
        todayKey -> stringResource(id = R.string.record_date_today)
        yesterdayKey -> stringResource(id = R.string.record_date_yesterday)
        else -> purchaseRecordDateFormatter.format(Date(dayKey))
    }
}

private val purchaseRecordDateFormatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
private val purchaseRecordPriceFormat = DecimalFormat("#.##")

private fun formatPurchaseRecordPrice(value: Float): String = purchaseRecordPriceFormat.format(value)

@Preview(showBackground = true)
@Composable
private fun ListProductPurchaseRecordViewPreview() {
    val today = System.currentTimeMillis()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis

    val samplePurchases = listOf(
        PurchaseForStoreCore(1, 1, "Samsung Galaxy", 800f, 2, today, true),
        PurchaseForStoreCore(2, 2, "iPhone 15", 900f, 1, today - 3_600_000, false),
        PurchaseForStoreCore(3, 3, "Auriculares", 20f, 3, yesterday, true),
    )

    AgendaDeVentasTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ListProductPurchaseRecordView(purchases = samplePurchases)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListProductPurchaseRecordViewEmptyPreview() {
    AgendaDeVentasTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ListProductPurchaseRecordView(purchases = emptyList())
        }
    }
}
