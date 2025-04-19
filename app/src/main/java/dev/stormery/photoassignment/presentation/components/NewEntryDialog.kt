package dev.stormery.photoassignment.presentation.components

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.stormery.photoassignment.R
import dev.stormery.photoassignment.presentation.MainScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewEntryDialog(
) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val showDialog = viewModel.showNewEntryDialog.collectAsState().value
    if(showDialog){
        Dialog(
            onDismissRequest = {
                viewModel.hideNewEntryDialog()
            },
        ) {
            Column(
                Modifier.fillMaxSize().padding(16.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().weight(1f).padding(16.dp)
                        .border(1.dp, color = Color.Black)
                ){
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxSize()
                    )
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
                            // Handle save action
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