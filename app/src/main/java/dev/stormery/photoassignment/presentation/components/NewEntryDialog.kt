package dev.stormery.photoassignment.presentation.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.stormery.photoassignment.R
import dev.stormery.photoassignment.core.Consts.CAMERA_PERMISSION
import dev.stormery.photoassignment.core.Consts.getVideoPermissions
import dev.stormery.photoassignment.presentation.utils.CameraHelper
import dev.stormery.photoassignment.presentation.MainScreenViewModel
import dev.stormery.photoassignment.presentation.camera.UIEvent
import dev.stormery.photoassignment.presentation.NewJournalViewModel
import dev.stormery.photoassignment.presentation.components.permissions.PermissionHandler
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun NewEntryDialog(
    onDismissRequest : () -> Unit = {},
) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val newJournalViewModel: NewJournalViewModel = koinViewModel()
    val showDialog = viewModel.showNewEntryDialog.collectAsState().value
    val journalData = newJournalViewModel.journalData.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity
    val storagePermission = getVideoPermissions()

    val cameraPermissionHandler = remember {
        PermissionHandler(
            activity = activity,
            permission = Manifest.permission.CAMERA,
            onGranted = {
                newJournalViewModel.onCameraPermissionGranted()
            },
            onDenied = { newJournalViewModel.onPermissionDenied() },
            onPermanentlyDenied = {
                newJournalViewModel.onPermanentlyDenied()
            }
        )
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionHandler.handleResult(isGranted)
    }

    val storagePermissionHandler = remember {
        PermissionHandler(
            activity = activity,
            permission = storagePermission,
            onGranted = {
                cameraPermissionLauncher.launch(CAMERA_PERMISSION)
            },
            onDenied = { newJournalViewModel.onPermissionDenied() },
            onPermanentlyDenied = {
                newJournalViewModel.onPermanentlyDenied()
            }
        )
    }
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        storagePermissionHandler.handleResult(isGranted)
    }


    val videoSuccessfullyCaptured = remember { mutableStateOf(false) }

    val videoCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) {
            // Video capture was successful
            journalData.value.videoUri?.let { uri ->
                val file = File(uri.path ?: return@let)
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(file.absolutePath),
                    arrayOf("video/mp4"),
                    null
                )
                videoSuccessfullyCaptured.value = true
            }
        } else {
            // Handle the failure
            videoSuccessfullyCaptured.value = false
            journalData.value.videoUri?.let {
                context.contentResolver.delete(it, null, null)
            }
        }
    }

    LaunchedEffect(Unit) {
        newJournalViewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.StartVideoCapture -> {
                    val timestamp = System.currentTimeMillis()
                    newJournalViewModel.setTimestamp(timestamp)
                    val videoFile = CameraHelper().createVideoFile(context, timestamp.toString())
                    newJournalViewModel.setVideoPath(videoFile.absolutePath)
                    val uri = CameraHelper().getVideoUri(context, videoFile)
                    videoCaptureLauncher.launch(uri)
                    newJournalViewModel.setVideoUri(uri)
                }
                is UIEvent.ShowPermissionDenied -> {
                    // Show permission denied message
                    Toast.makeText(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
                is UIEvent.OnPermanentlyDenied -> {
                    Toast.makeText(context, context.getString(R.string.enable_permissons_in_settings), Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    if(showDialog){
        Dialog(
            onDismissRequest = {
                newJournalViewModel.resetEntryState()
                viewModel.hideNewEntryDialog()
                onDismissRequest()
            },
        ) {
            Column(
                Modifier.fillMaxSize().padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, color = Color.Black)
                    .clip(RoundedCornerShape(16.dp))
            ) {

                if(journalData.value.videoUri != null && videoSuccessfullyCaptured.value){
                    Column(
                        Modifier.fillMaxWidth().weight(1f).padding(16.dp)
                            .border(1.dp, color = Color.Black).clip(RoundedCornerShape(16.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VideoPlayer(uri = journalData.value.videoUri!! )
                    }
                }else{
                    Column(
                        Modifier.fillMaxWidth().weight(1f).padding(16.dp)
                            .border(1.dp, color = Color.Black).clip(RoundedCornerShape(16.dp))
                            .clickable {
                                storagePermissionHandler.checkAndRequest(storagePermissionLauncher)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.camera),

                            contentDescription = "Image",
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
                        )
                        Text(
                            text = stringResource(R.string.click_to_take_a_video),
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
                            label = { Text(stringResource(R.string.description_text))},
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).imePadding(),
                        )

                    Button(
                        onClick = {
                            newJournalViewModel.saveJournal{
                                viewModel.hideNewEntryDialog()
                            }
                        },
                        enabled = videoSuccessfullyCaptured.value,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                            .padding(8.dp).imePadding(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Text(stringResource(R.string.save_text))
                    }
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                    Button(
                        onClick = {
                            newJournalViewModel.resetEntryState()
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                            .padding(8.dp).imePadding(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Text(stringResource(R.string.cancel_text))
                    }
                }
            }

        }
    }
}
