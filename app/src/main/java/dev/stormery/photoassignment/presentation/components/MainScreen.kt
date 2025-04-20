package dev.stormery.photoassignment.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stormery.photoassignment.presentation.MainScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = koinViewModel()
) {
    val journals = viewModel.journals.collectAsState(initial = emptyList())
    Box(
        modifier = modifier
    ){
        LazyColumn {
            item{
                Button(
                    onClick = {
                        viewModel.showNewEntryDialog()
                    },
                ) {
                    Text("Add New Entry")
                }
            }
            items(journals.value.size){
                val journal = journals.value[it]
                Text(
                    text = "Journal Entry: ${journal.description} + ${journal.timestamp}",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
    NewEntryDialog()
}