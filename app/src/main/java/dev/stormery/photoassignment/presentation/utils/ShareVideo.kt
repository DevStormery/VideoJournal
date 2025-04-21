package dev.stormery.photoassignment.presentation.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun shareVideo(context: Context, videoFile: File) {
    val videoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        videoFile
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "video/mp4"
        putExtra(Intent.EXTRA_STREAM, videoUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(shareIntent, "Share video via")
    )
}