package dev.stormery.photoassignment.core

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File


class CameraHelper {
    fun createVideoFile(context: Context, timestamp:String): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File.createTempFile("VIDEO_JOURNAL_${timestamp}_", ".mp4", storageDir)
    }

    fun getVideoUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}