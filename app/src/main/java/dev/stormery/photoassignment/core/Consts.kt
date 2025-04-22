package dev.stormery.photoassignment.core

import android.os.Build

object Consts {
    const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    const val READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    const val READ_MEDIA_VIDEO_PERMISSION = android.Manifest.permission.READ_MEDIA_VIDEO

    fun getVideoPermissions(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            READ_MEDIA_VIDEO_PERMISSION
        } else {
            READ_EXTERNAL_STORAGE_PERMISSION
        }
    }
}