package com.virtualworld.agendadeventas.ui.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.virtualworld.agendadeventas.R

@Composable
fun StoreSelectView(
    modifier: Modifier = Modifier,
    label: String,
    stores: List<Pair<Int, String>>,
    selectedIndex: Int,
    isLoading: Boolean,
    placeholder: String,
    onStoreSelected: (index: Int) -> Unit,
) {
    var showStorePicker by remember { mutableStateOf(false) }

    val storeText by remember(stores, selectedIndex) {
        derivedStateOf {
            if (selectedIndex in stores.indices) stores[selectedIndex].second else placeholder
        }
    }

    val hasSelection = selectedIndex in stores.indices

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
        )

        Surface(
            onClick = { if (!isLoading) showStorePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.14f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(
                        text = storeText,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (hasSelection) FontWeight.SemiBold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = if (hasSelection) 1f else 0.75f,
                        ),
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector = Icons.Outlined.Storefront,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                    )
                }
            }
        }
    }

    if (showStorePicker) {
        StorePickerDialog(
            title = label,
            stores = stores,
            selectedIndex = selectedIndex,
            isLoading = isLoading,
            onDismiss = { showStorePicker = false },
            onStoreSelected = { index ->
                onStoreSelected(index)
                showStorePicker = false
            },
        )
    }
}

@Composable
private fun StorePickerDialog(
    title: String,
    stores: List<Pair<Int, String>>,
    selectedIndex: Int,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onStoreSelected: (index: Int) -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary
    val listState = rememberLazyListState()

    if (selectedIndex > 0) {
        LaunchedEffect(selectedIndex) {
            listState.scrollToItem(selectedIndex)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = primary,
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = primary)
                    }
                } else if (stores.isEmpty()) {
                    Text(
                        text = stringResource(R.string.sell_no_stores),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        state = listState,
                    ) {
                        itemsIndexed(stores) { index, store ->
                            StorePickerItem(
                                name = store.second,
                                selected = index == selectedIndex,
                                onClick = { onStoreSelected(index) },
                            )
                            if (index < stores.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StorePickerItem(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary

    Surface(
        onClick = onClick,
        color = if (selected) primary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Storefront,
                contentDescription = null,
                tint = if (selected) primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (selected) primary else MaterialTheme.colorScheme.onSurface,
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = primary,
                )
            }
        }
    }
}
