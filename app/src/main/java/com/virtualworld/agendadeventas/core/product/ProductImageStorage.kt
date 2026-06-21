package com.virtualworld.agendadeventas.core.product

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductImageStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val imagesDir: File
        get() = File(context.filesDir, DIR_NAME).apply { mkdirs() }

    fun createCaptureTarget(): Pair<File, Uri> {
        val file = File(imagesDir, "product_${UUID.randomUUID()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
        return file to uri
    }

    fun toRelativePath(file: File): String =
        file.relativeTo(context.filesDir).path.replace('\\', '/')

    fun resolveFile(relativePath: String?): File? {
        if (relativePath.isNullOrBlank()) return null
        val file = File(context.filesDir, relativePath)
        return file.takeIf { it.exists() }
    }

    fun deleteImage(relativePath: String?) {
        resolveFile(relativePath)?.delete()
    }

    companion object {
        private const val DIR_NAME = "product_images"
    }
}
