package dev.stormery.photoassignment.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.stormery.photoassignment.R
import dev.stormery.photoassignment.core.toFormattedDate
import dev.stormery.photoassignment.presentation.model.JournalData

@Composable
fun JournalEntryRow(journalData: JournalData) {
    Row(
        Modifier.fillMaxWidth().padding(8.dp)
    ){
        Column(
            modifier = Modifier.weight(1f).padding(8.dp)
        ){
            journalData.videoUri?.let { VideoPlayer(uri = it) }?:run {
                Image(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "Image",
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                journalData.timestamp.toFormattedDate(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
            journalData.description?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontStyle = FontStyle.Italic
                    )
                )
            }?:run {
                Text(
                    text = "No description",
                    style = TextStyle(
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }

    }
}