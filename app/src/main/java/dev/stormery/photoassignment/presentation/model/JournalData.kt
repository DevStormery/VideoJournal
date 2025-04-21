package dev.stormery.photoassignment.presentation.model

import android.net.Uri

data class JournalData(
    val id: Long = 0,
    val videoUri: Uri? = null,
    val videoPath: String = "",
    val description: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)