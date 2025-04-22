package dev.stormery.photoassignment.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.stormery.photoassignment.R

@Composable
fun ExpandableText(
    text: String,
    collapsedMaxLines: Int = 3,
    style: TextStyle = TextStyle()
) {
    var isExpanded by remember { mutableStateOf(false) }
    var hasOverflow by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded } // Toggle on text click
            .animateContentSize()
    ) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = style,
            onTextLayout = { textLayoutResult ->
                hasOverflow = textLayoutResult.hasVisualOverflow
            }
        )

        if (hasOverflow) {
            Text(
                text = if (isExpanded) stringResource(R.string.show_less_text) else stringResource(R.string.read_more_text),
                style = style.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

