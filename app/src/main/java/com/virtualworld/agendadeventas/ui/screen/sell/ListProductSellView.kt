package com.virtualworld.agendadeventas.ui.screen.sell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.virtualworld.agendadeventas.core.model.ProductStoreCore
import com.virtualworld.agendadeventas.ui.screen.common.ProductAvatar
import com.virtualworld.agendadeventas.ui.theme.AgendaDeVentasTheme
import java.text.DecimalFormat

@Composable
fun ListProductSellView(
    products: List<ProductStoreCore>,
    changeUnitSell: (Pair<Int, Int>) -> Unit,
    listChangerProductsSell: List<Pair<Int, Int>>,
    modifier: Modifier = Modifier,
) {
    val displayProducts = remember(products) {
        products.filter { it.idProduct != 0 && it.nombre.isNotBlank() }
    }

    val quantitiesByProductId = remember(listChangerProductsSell) {
        listChangerProductsSell.associate { it.first to it.second }
    }

    val selectedCount = remember(quantitiesByProductId) {
        quantitiesByProductId.values.count { it > 0 }
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
        if (displayProducts.isEmpty()) {
            item {
                SellEmptyProductsState()
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
                            id = R.plurals.edit_products_count,
                            count = displayProducts.size,
                            displayProducts.size,
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    if (selectedCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingCart,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp),
                                )
                                Text(
                                    text = pluralStringResource(
                                        id = R.plurals.sell_selected_count,
                                        count = selectedCount,
                                        selectedCount,
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
            }

            items(
                items = displayProducts,
                key = { it.idProduct },
            ) { product ->
                SellProductListItem(
                    product = product,
                    quantity = quantitiesByProductId[product.idProduct] ?: 0,
                    onQuantityChange = changeUnitSell,
                )
            }

        }
    }
}

@Composable
private fun SellProductListItem(
    product: ProductStoreCore,
    quantity: Int,
    onQuantityChange: (Pair<Int, Int>) -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    val isSelected = quantity > 0
    val isAtStockLimit = product.tieneInventario && quantity >= product.inventario
    val targetBorderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
    }
    val targetContainerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(durationMillis = 280),
        label = "sellItemBorderColor",
    )
    val containerColor by animateColorAsState(
        targetValue = targetContainerColor,
        animationSpec = tween(durationMillis = 280),
        label = "sellItemContainerColor",
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor, shape = shape),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProductAvatar(
                    name = product.nombre,
                    imagePath = product.imagePath,
                    size = 48.dp,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp),
                ) {
                    Text(
                        text = product.nombre,
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
                            if (product.tieneInventario) {
                                SellInventoryChip(inventario = product.inventario)
                            }
                            SellPriceChip(
                                value = formatSellPrice(product.venta),
                                highlighted = true,
                            )
                        }
                }

                SellQuantityStepper(
                    quantity = quantity,
                    increaseEnabled = !isAtStockLimit,
                    onDecrease = { onQuantityChange(Pair(product.idProduct, -1)) },
                    onIncrease = { onQuantityChange(Pair(product.idProduct, 1)) },
                )
            }

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(animationSpec = tween(220)) + expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                    expandFrom = Alignment.Top,
                ),
                exit = fadeOut(animationSpec = tween(180)) + shrinkVertically(
                    animationSpec = tween(200),
                    shrinkTowards = Alignment.Top,
                ),
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.sell_line_subtotal,
                            quantity,
                            formatSellPrice(product.venta * quantity),
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
}

@Composable
private fun SellQuantityStepper(
    quantity: Int,
    increaseEnabled: Boolean = true,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilledIconButton(
                onClick = onDecrease,
                enabled = quantity > 0,
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
                    contentDescription = stringResource(id = R.string.sell_decrease_quantity),
                    modifier = Modifier.size(18.dp),
                )
            }

            val quantityColor by animateColorAsState(
                targetValue = if (quantity > 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                animationSpec = tween(durationMillis = 280),
                label = "sellQuantityColor",
            )

            Text(
                text = quantity.toString(),
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = quantityColor,
                textAlign = TextAlign.Center,
                minLines = 1,
            )

            FilledIconButton(
                onClick = onIncrease,
                enabled = increaseEnabled,
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
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.sell_increase_quantity),
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun SellInventoryChip(
    inventario: Int,
) {
    val highlighted = inventario == 0
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (highlighted) {
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.65f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                tint = if (highlighted) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = inventario.toString(),
                modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Medium,
                color = if (highlighted) {
                    MaterialTheme.colorScheme.error
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
private fun SellPriceChip(
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
                text = value,
                modifier = if (highlighted) Modifier.padding(start = 4.dp) else Modifier,
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
private fun SellEmptyProductsState() {
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
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )
            }
            Text(
                text = stringResource(id = R.string.sell_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(id = R.string.sell_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private val sellPriceFormat = DecimalFormat("#.##")

private fun formatSellPrice(value: Float): String = sellPriceFormat.format(value)

@Preview(showBackground = true)
@Composable
fun ListProductSellViewPreview() {
    val sampleProducts = listOf(
        ProductStoreCore(1, "Product 1", 10.0f, 15.0f),
        ProductStoreCore(2, "Product 2", 20.0f, 25.0f),
        ProductStoreCore(3, "Product 3", 5.0f, 8.0f)
    )
    AgendaDeVentasTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ListProductSellView(
                products = sampleProducts,
                changeUnitSell = {},
                listChangerProductsSell = listOf(Pair(1, 2), Pair(2, 0))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListProductSellViewEmptyPreview() {
    AgendaDeVentasTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ListProductSellView(
                products = emptyList(),
                changeUnitSell = {},
                listChangerProductsSell = emptyList()
            )
        }
    }
}

@Preview(showBackground = true, name = "Sell Price Chip")
@Composable
private fun SellPriceChipPreview() {
    AgendaDeVentasTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SellPriceChip(
                value = "10.00",
                highlighted = false
            )
            SellPriceChip(
                value = "15.00",
                highlighted = true
            )
        }
    }
}

@Preview(showBackground = true, name = "Sell Product List Item - With Inventory")
@Composable
private fun SellProductListItemWithInventoryPreview() {
    AgendaDeVentasTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SellProductListItem(
                product = ProductStoreCore(
                    idProduct = 3,
                    nombre = "Camiseta",
                    compra = 5.0f,
                    venta = 12.0f,
                    tieneInventario = true,
                    inventario = 24,
                ),
                quantity = 0,
                onQuantityChange = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Sell Product List Item - No Inventory Control")
@Composable
private fun SellProductListItemNoInventoryPreview() {
    AgendaDeVentasTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SellProductListItem(
                product = ProductStoreCore(4, "Servicio de consultoría", 0f, 50.0f),
                quantity = 0,
                onQuantityChange = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Sell Product List Item - Not Selected")
@Composable
private fun SellProductListItemNotSelectedPreview() {
    AgendaDeVentasTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SellProductListItem(
                product = ProductStoreCore(1, "Samsung Galaxy S23 Ultra", 800.0f, 1200.0f),
                quantity = 0,
                onQuantityChange = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Sell Product List Item - Selected")
@Composable
private fun SellProductListItemSelectedPreview() {
    AgendaDeVentasTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SellProductListItem(
                product = ProductStoreCore(2, "iPhone 15 Pro Max", 900.0f, 1300.0f),
                quantity = 3,
                onQuantityChange = {}
            )
        }
    }
}
