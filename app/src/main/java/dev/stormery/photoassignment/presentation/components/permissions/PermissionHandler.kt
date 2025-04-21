package dev.stormery.photoassignment.presentation.components.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler(
    private val activity: Activity,
    private val permission: String,
    private val onGranted: () -> Unit,
    private val onDenied: (() -> Unit)? = null,
    private val onPermanentlyDenied: (() -> Unit)? = null
) {
    fun checkAndRequest(launcher: ManagedActivityResultLauncher<String, Boolean>) {
        when {
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
                onGranted()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                // Show rationale if needed, then launch
                launcher.launch(permission)
            }
            else -> {
                // First time or permanently denied
                launcher.launch(permission)
            }
        }
    }

    fun handleResult(isGranted: Boolean) {
        if (isGranted) {
            onGranted()
        } else {
            val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            if (!shouldShowRationale) {
                onPermanentlyDenied?.invoke()
            } else {
                onDenied?.invoke()
            }
        }
    }
}
