package com.virtualworld.agendadeventas.ui.screen.resume

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.virtualworld.agendadeventas.R

@Composable
fun CardResumeStores(
    title: String,
    colum2: String,
    detalleCol1: List<String>,
    detalleCol2: List<String>,
    infoDescription: String,
    metricKind: ResumeMetricKind,
) {
    val storeItems = detalleCol1.indices.mapNotNull { index ->
        val storeName = detalleCol1.getOrNull(index).orEmpty()
        if (storeName.isBlank()) return@mapNotNull null
        storeName to detalleCol2.getOrElse(index) { "" }
    }
    val totalValue = remember(storeItems, metricKind) {
        computeResumeTotal(metricKind, storeItems.map { it.second })
    }
    val cardShape = RoundedCornerShape(16.dp)
    val cardBorderColor = MaterialTheme.colorScheme.primary.copy(
        alpha = if (isSystemInDarkTheme()) 0.45f else 0.30f,
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(width = 1.5.dp, color = cardBorderColor, shape = cardShape),
        shape = cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ResumeCardHeader(
                title = title,
                infoDescription = infoDescription,
            )

            if (storeItems.isEmpty()) {
                ResumeCardEmptyState()
            } else {
                ResumeColumnHeader(
                    storeLabel = stringResource(id = R.string.inicio_info_tiendas),
                    valueLabel = colum2,
                )

                storeItems.forEachIndexed { index, (storeName, value) ->
                    ResumeStoreItem(
                        storeName = storeName,
                        value = value,
                        metricKind = metricKind,
                    )
                    if (index < storeItems.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                        )
                    }
                }

                ResumeTotalRow(
                    totalValue = totalValue,
                    metricKind = metricKind,
                )
            }
        }
    }
}

@Composable
private fun ResumeMetricKind.chipColors(emphasized: Boolean = false): Pair<Color, Color> {
    val alpha = if (emphasized) 1f else 0.75f
    return when (this) {
        ResumeMetricKind.Units -> {
            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = alpha) to
                MaterialTheme.colorScheme.onTertiaryContainer
        }
        ResumeMetricKind.Amount -> {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha) to
                MaterialTheme.colorScheme.primary
        }
        ResumeMetricKind.Investment -> {
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = alpha) to
                MaterialTheme.colorScheme.onSecondaryContainer
        }
        ResumeMetricKind.Profit -> {
            if (isSystemInDarkTheme()) {
                Color(0xFF1B3A24).copy(alpha = alpha) to Color(0xFF81C784)
            } else {
                Color(0xFFE8F5E9).copy(alpha = alpha) to Color(0xFF2E7D32)
            }
        }
    }
}

@Composable
private fun ResumeValueChip(
    value: String,
    metricKind: ResumeMetricKind,
    emphasized: Boolean = false,
) {
    val (containerColor, contentColor) = metricKind.chipColors(emphasized = emphasized)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = containerColor,
    ) {
        Text(
            text = value,
            modifier = Modifier.padding(
                horizontal = if (emphasized) 14.dp else 12.dp,
                vertical = if (emphasized) 8.dp else 6.dp,
            ),
            style = if (emphasized) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.titleSmall
            },
            fontWeight = FontWeight.Bold,
            color = contentColor,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ResumeCardHeader(
    title: String,
    infoDescription: String,
) {
    var showInfoDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            IconButton(onClick = { showInfoDialog = true }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(id = R.string.inicio_info_icon),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(text = title) },
            text = { Text(text = infoDescription) },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(text = stringResource(id = R.string.inicio_info_cerrar))
                }
            },
        )
    }
}

@Composable
private fun ResumeColumnHeader(
    storeLabel: String,
    valueLabel: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Store,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = storeLabel,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = valueLabel,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}

@Composable
private fun ResumeStoreItem(
    storeName: String,
    value: String,
    metricKind: ResumeMetricKind,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ResumeStoreAvatar(storeName = storeName)

        Text(
            text = storeName,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        ResumeValueChip(
            value = value,
            metricKind = metricKind,
        )
    }
}

@Composable
private fun ResumeTotalRow(
    totalValue: String,
    metricKind: ResumeMetricKind,
) {
    HorizontalDivider(
        modifier = Modifier.padding(top = 4.dp),
        thickness = 2.dp,
        color = MaterialTheme.colorScheme.outlineVariant,
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.inicio_resume_total),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            ResumeValueChip(
                value = totalValue,
                metricKind = metricKind,
                emphasized = true,
            )
        }
    }
}

@Composable
private fun ResumeStoreAvatar(storeName: String) {
    val initial = storeName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
private fun ResumeCardEmptyState() {
    Text(
        text = stringResource(id = R.string.inicio_resume_no_stores),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}
