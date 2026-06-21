package com.virtualworld.agendadeventas.ui.screen.common

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.virtualworld.agendadeventas.R
import com.virtualworld.agendadeventas.core.product.ProductImageStorage
import java.io.File

@Composable
fun ProductPhotoPicker(
    productName: String,
    imagePath: String?,
    onImagePathChange: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageStorage = remember { ProductImageStorage(context) }
    var pendingCaptureFile by remember { mutableStateOf<File?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        val file = pendingCaptureFile
        pendingCaptureFile = null
        if (success && file != null) {
            val previousPath = imagePath
            onImagePathChange(imageStorage.toRelativePath(file))
            if (previousPath != null && previousPath != imageStorage.toRelativePath(file)) {
                imageStorage.deleteImage(previousPath)
            }
        } else {
            file?.delete()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            val (file, uri) = imageStorage.createCaptureTarget()
            pendingCaptureFile = file
            takePictureLauncher.launch(uri)
        }
    }

    fun launchCamera() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            val (file, uri) = imageStorage.createCaptureTarget()
            pendingCaptureFile = file
            takePictureLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ProductAvatar(
            name = productName,
            imagePath = imagePath,
            size = 88.dp,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = { launchCamera() }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(18.dp),
                )
                Text(text = stringResource(id = R.string.product_photo_take))
            }

            if (imagePath != null) {
                TextButton(
                    onClick = {
                        imageStorage.deleteImage(imagePath)
                        onImagePathChange(null)
                    },
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(18.dp),
                    )
                    Text(text = stringResource(id = R.string.product_photo_remove))
                }
            }
        }
    }
}
