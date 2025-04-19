package dev.stormery.photoassignment.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.stormery.photoassignment.presentation.MainScreenViewModel

@Composable
fun CameraPermissionHandler(viewModel: MainScreenViewModel) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted->
        if (isGranted) {
            // Permission is granted, proceed with camera access
            //viewModel.onCameraPermissionGranted()
        } else {
            // Permission is denied, show a message or handle accordingly
            //viewModel.onCameraPermissionDenied()
        }

    }

}