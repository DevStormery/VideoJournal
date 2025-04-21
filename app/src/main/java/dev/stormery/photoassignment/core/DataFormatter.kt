package dev.stormery.photoassignment.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(this))
}