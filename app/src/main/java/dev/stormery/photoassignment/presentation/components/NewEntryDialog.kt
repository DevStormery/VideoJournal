package dev.stormery.photoassignment.presentation.components

import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.stormery.photoassignment.R
import dev.stormery.photoassignment.core.CameraHelper
import dev.stormery.photoassignment.presentation.MainScreenViewModel
import dev.stormery.photoassignment.presentation.camera.CameraUIEvent
import dev.stormery.photoassignment.presentation.camera.CameraViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NewEntryDialog(
) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val showDialog = viewModel.showNewEntryDialog.collectAsState().value
    val context = LocalContext.current

    val cameraViewModel = koinViewModel<CameraViewModel>()
    val videoUri by cameraViewModel.videoUri.collectAsState()
    val videoSuccessfullyCaptured = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted->
        if (isGranted) {
            // Permission is granted, proceed with camera access
            cameraViewModel.onCameraPermissionGranted()
        } else {
            // Permission is denied, show a message or handle accordingly
            cameraViewModel.onCameraPermissionDenied()
        }
    }
    val videoCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) {
            // Video capture was successful
            videoSuccessfullyCaptured.value = true
        } else {
            // Handle the failure
            videoSuccessfullyCaptured.value = false
        }
    }
    LaunchedEffect(Unit) {
        cameraViewModel.uiEvent.collect { event ->
            when (event) {
                is CameraUIEvent.StartVideoCapture -> {
                    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                    val videoFile = CameraHelper().createVideoFile(context, timestamp)
                    val uri = CameraHelper().getVideoUri(context, videoFile)
                    videoCaptureLauncher.launch(uri)
                    cameraViewModel.setVideoUri(uri)
                }
                is CameraUIEvent.ShowPermissionDeniedMessage -> {
                    // Show permission denied message
                    Toast.makeText(
                        context,
                        "Camera permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is CameraUIEvent.ShowError -> {
                    // Show error message
                }
            }
        }
    }

    if(showDialog){
        Dialog(
            onDismissRequest = {
                viewModel.hideNewEntryDialog()
            },
        ) {
            Column(
                Modifier.fillMaxSize().padding(16.dp)
                    .background(Color.White)
                    .border(1.dp, color = Color.Black)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(
                    Modifier.fillMaxWidth().weight(1f).padding(16.dp)
                        .border(1.dp, color = Color.Black)
                ){
                    if(videoUri != null && videoSuccessfullyCaptured.value){
                        VideoPlayer(uri = videoUri!! )
                    }else{
                        Image(
                            painter = painterResource(R.drawable.camera),
                            contentDescription = "Image",
                            modifier = Modifier.fillMaxSize().clickable {
                                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        )
                    }
                }
                Column(
                    Modifier.fillMaxWidth().weight(1f).padding(16.dp)
                ){
                    val description = remember { mutableStateOf("") }
                    TextField(
                        value = description.value,
                        onValueChange = {
                            description.value = it
                        },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).imePadding(),

                        )

                    Button(
                        onClick = {
                            viewModel.addJournal(
                                filePath = videoUri.toString(),
                                description = description.value,
                                timestamp = System.currentTimeMillis()
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Save")
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun NewEntryDialogPreview() {
    NewEntryDialog()
}