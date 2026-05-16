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
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ReceiptLong
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
import com.virtualworld.agendadeventas.core.model.SoldForStoreCore
import com.virtualworld.agendadeventas.ui.theme.AgendaDeVentasTheme
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private data class RecordDateSection(
    val dayKey: Long,
    val sales: List<SoldForStoreCore>,
)

@Composable
fun ListProductRecordView(
    stateScreenProductStore: List<SoldForStoreCore>,
    modifier: Modifier = Modifier,
) {
    val sections = remember(stateScreenProductStore) {
        groupRecordsByDate(stateScreenProductStore)
    }
    val totalSales = remember(sections) {
        sections.sumOf { it.sales.size }
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
                RecordEmptyState()
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
                            id = R.plurals.record_sales_count,
                            count = totalSales,
                            totalSales,
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
                    RecordDateSeparator(
                        dayKey = section.dayKey,
                        salesCount = section.sales.size,
                    )
                }

                items(
                    items = section.sales,
                    key = { sale -> sale.idbd },
                ) { sale ->
                    RecordSaleListItem(sale = sale)
                }
            }
        }
    }
}

@Composable
private fun RecordDateSeparator(
    dayKey: Long,
    salesCount: Int,
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
                text = formatRecordSectionDate(dayKey),
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
                        id = R.plurals.record_day_sales_count,
                        count = salesCount,
                        salesCount,
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
private fun RecordSaleListItem(sale: SoldForStoreCore) {
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
                RecordProductAvatar(name = sale.nombre)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp),
                ) {
                    Text(
                        text = sale.nombre,
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
                        RecordInfoChip(
                            label = stringResource(id = R.string.record_unidades) + " ",
                            value = sale.unidades.toString(),
                        )
                        RecordInfoChip(
                            label = stringResource(id = R.string.record_costo) + " ",
                            value = formatRecordPrice(sale.compra),
                        )
                        RecordInfoChip(
                            label = stringResource(id = R.string.record_precio) + " ",
                            value = formatRecordPrice(sale.valor),
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
                        sale.unidades,
                        formatRecordPrice(sale.valor),
                        formatRecordPrice(sale.valor * sale.unidades),
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
        }
    }
}

@Composable
private fun RecordInfoChip(
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
private fun RecordProductAvatar(name: String) {
    val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun RecordEmptyState() {
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
                    imageVector = Icons.Outlined.ReceiptLong,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )
            }
            Text(
                text = stringResource(id = R.string.record_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(id = R.string.record_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun groupRecordsByDate(records: List<SoldForStoreCore>): List<RecordDateSection> {
    return records
        .filter { it.idbd != 0L || it.nombre.isNotBlank() }
        .groupBy { dayKey(it.fecha) }
        .map { (key, sales) ->
            RecordDateSection(
                dayKey = key,
                sales = sales.sortedByDescending { it.fecha },
            )
        }
        .sortedByDescending { it.dayKey }
}

private fun dayKey(timestamp: Long): Long {
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
private fun formatRecordSectionDate(dayKey: Long): String {
    val todayKey = dayKey(System.currentTimeMillis())
    val yesterdayKey = dayKey(
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis,
    )

    return when (dayKey) {
        todayKey -> stringResource(id = R.string.record_date_today)
        yesterdayKey -> stringResource(id = R.string.record_date_yesterday)
        else -> recordDateFormatter.format(Date(dayKey))
    }
}

private val recordDateFormatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
private val recordPriceFormat = DecimalFormat("#.##")

private fun formatRecordPrice(value: Float): String = recordPriceFormat.format(value)

@Preview(showBackground = true)
@Composable
private fun ListProductRecordViewPreview() {
    val today = System.currentTimeMillis()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis

    val sampleSales = listOf(
        SoldForStoreCore(1, 1, "Samsung Galaxy", 800f, 1200f, 2, today),
        SoldForStoreCore(2, 2, "iPhone 15", 900f, 1300f, 1, today - 3_600_000),
        SoldForStoreCore(3, 3, "Auriculares", 20f, 45f, 3, yesterday),
    )

    AgendaDeVentasTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ListProductRecordView(stateScreenProductStore = sampleSales)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListProductRecordViewEmptyPreview() {
    AgendaDeVentasTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ListProductRecordView(stateScreenProductStore = emptyList())
        }
    }
}
