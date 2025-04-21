package dev.stormery.photoassignment.presentation.components

import android.Manifest
import android.os.Build
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
import androidx.compose.ui.Alignment
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
import dev.stormery.photoassignment.presentation.NewJournalViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NewEntryDialog(
) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val newJournalViewModel: NewJournalViewModel = koinViewModel()
    val showDialog = viewModel.showNewEntryDialog.collectAsState().value
    val journalData = newJournalViewModel.journalData.collectAsState()
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Proceed with camera action
            newJournalViewModel.onCameraPermissionGranted()
        } else {
            newJournalViewModel.onCameraPermissionDenied()
        }
    }
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Now ask for camera
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            // Handle storage denied
            Toast.makeText(context, "Storage permission denied", Toast.LENGTH_SHORT).show()
        }
    }




    val videoSuccessfullyCaptured = remember { mutableStateOf(false) }

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
        newJournalViewModel.uiEvent.collect { event ->
            when (event) {
                is CameraUIEvent.StartVideoCapture -> {
                    val timestamp = System.currentTimeMillis()
                    newJournalViewModel.setTimestamp(timestamp)
                    val videoFile = CameraHelper().createVideoFile(context, timestamp.toString())
                    val uri = CameraHelper().getVideoUri(context, videoFile)
                    videoCaptureLauncher.launch(uri)
                    newJournalViewModel.setVideoUri(uri)
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
                newJournalViewModel.resetEntryState()
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
                        .border(1.dp, color = Color.Black),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if(journalData.value.videoUri != null && videoSuccessfullyCaptured.value){
                        VideoPlayer(uri = journalData.value.videoUri!! )
                    }else{
                        Image(
                            painter = painterResource(R.drawable.camera),
                            contentDescription = "Image",
                            modifier = Modifier.fillMaxWidth().clickable {
                                val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    Manifest.permission.READ_MEDIA_VIDEO
                                } else {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                                storagePermissionLauncher.launch(storagePermission)
                            }
                        )
                        Text(
                            text = "Click to take a video",
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }
                Column(
                    Modifier.fillMaxWidth().weight(1f).padding(16.dp)
                ){
                    TextField(
                            value = journalData.value.description?: "",
                            onValueChange = {
                                newJournalViewModel.setDescription(it)
                            },
                            label = { Text("Description (optional)") },
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).imePadding(),
                        )

                    Button(
                        onClick = {
                            newJournalViewModel.saveJournal{
                                viewModel.hideNewEntryDialog()
                            }
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