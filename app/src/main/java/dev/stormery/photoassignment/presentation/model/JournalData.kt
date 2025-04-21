package dev.stormery.photoassignment.presentation.model

import android.net.Uri

data class JournalData(
    val videoUri: Uri? = null,
    val description: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)