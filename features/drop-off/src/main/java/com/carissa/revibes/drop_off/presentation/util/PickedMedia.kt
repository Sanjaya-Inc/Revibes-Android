package com.carissa.revibes.drop_off.presentation.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File

data class CachedMedia(
    val uri: Uri,
    val contentType: String
)

internal fun cachePickedMedia(context: Context, uri: Uri): CachedMedia {
    val contentType = uriContentType(context, uri)
    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentType) ?: "jpg"
    val dest = File(context.cacheDir, "drop-off-${System.nanoTime()}.$extension")
    context.contentResolver.openInputStream(uri)?.use { input ->
        dest.outputStream().use { output -> input.copyTo(output) }
    } ?: error("Unable to read image")
    return CachedMedia(Uri.fromFile(dest), contentType)
}

internal fun uriContentType(context: Context, uri: Uri): String {
    return normalizedContentType(context.contentResolver.getType(uri))
}
