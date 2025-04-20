package dev.stormery.photoassignment.presentation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stormery.photoassignment.domain.usecase.AddJournalUseCase
import dev.stormery.photoassignment.domain.usecase.GetJournalsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val addJournalUseCase: AddJournalUseCase,
    private val getJournalsUseCase: GetJournalsUseCase
) : ViewModel() {

    val _showNewEntryDialog = MutableStateFlow(false)
    val showNewEntryDialog = _showNewEntryDialog.asStateFlow()

    val journals = getJournalsUseCase()

    fun showNewEntryDialog() {
        _showNewEntryDialog.value = true
    }
    fun hideNewEntryDialog() {
        _showNewEntryDialog.value = false
    }

    fun addJournal(filePath: String, description: String?, timestamp: Long) {
        viewModelScope.launch {
            addJournalUseCase(filePath, description, timestamp)
        }
    }
}