package dev.stormery.photoassignment.presentation.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stormery.photoassignment.R
import dev.stormery.photoassignment.presentation.MainScreenViewModel
import dev.stormery.photoassignment.presentation.utils.toFormattedDate
import dev.stormery.photoassignment.presentation.model.JournalData
import dev.stormery.photoassignment.presentation.utils.shareVideo
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun JournalEntryRow(journalData: JournalData) {
    val context = LocalContext.current
    val viewModel = koinViewModel<MainScreenViewModel>()
    Column(
        Modifier.fillMaxWidth().background(
            color = Color.Transparent,
            shape = MaterialTheme.shapes.large
        ).padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                journalData.timestamp.toFormattedDate(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
              //  .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ){
                journalData.videoUri?.let {
                    VideoPlayer(uri = it)
                } ?: Image(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            //Icons
            Row(
                Modifier.weight(1f).padding(start = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Edit",
                    modifier = Modifier.size(24.dp).clickable {
                        val videoFile = File( journalData.videoPath)
                        if(videoFile.exists()){
                            shareVideo(context,videoFile)
                        }else{
                            Toast.makeText(
                                context,
                                "Video file not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp).clickable{
                        viewModel.deleteJournal(journalData)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            journalData.description?.let {
                ExpandableText(text = it)
            }
        }

    }
}

