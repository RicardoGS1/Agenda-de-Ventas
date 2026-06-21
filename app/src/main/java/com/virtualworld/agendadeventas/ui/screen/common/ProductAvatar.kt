package com.virtualworld.agendadeventas.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.virtualworld.agendadeventas.core.product.ProductImageStorage

@Composable
fun ProductAvatar(
    name: String,
    imagePath: String?,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
) {
    val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val context = LocalContext.current
    val imageFile = remember(imagePath) {
        ProductImageStorage(context).resolveFile(imagePath)
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (imageFile != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageFile)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = initial,
                style = if (size >= 52.dp) {
                    MaterialTheme.typography.titleLarge
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
