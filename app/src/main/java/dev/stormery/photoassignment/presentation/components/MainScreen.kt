package dev.stormery.photoassignment.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stormery.photoassignment.presentation.MainScreenViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = koinViewModel()
) {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() {
        refreshing = true
        viewModel.onRefresh()
        refreshScope.launch {
            refreshing = false
        }
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)
    val journals = viewModel.journals.collectAsState(initial = emptyList())
    Box(
        modifier = modifier.pullRefresh(state)
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        onClick = {
                            viewModel.showNewEntryDialog()
                        },
                    ) {
                        Text("Add New Entry")
                    }
                }
            }
            when{
                journals.value.isEmpty() -> {
                    item {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                        ){
                            Text(
                                text = "No entries found",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
                else -> {
                    items(journals.value.size){
                        val journal = journals.value[it]
                        JournalEntryRow(journalData = journal)
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            }

        }

        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
    NewEntryDialog(
        onDismissRequest = {
            viewModel.loadJournals()
        }
    )
}