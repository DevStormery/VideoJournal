package dev.stormery.photoassignment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stormery.photoassignment.domain.usecase.DeleteJournalUseCase
import dev.stormery.photoassignment.domain.usecase.GetJournalsUseCase
import dev.stormery.photoassignment.presentation.model.JournalData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val getJournalsUseCase: GetJournalsUseCase,
    private val deleteJournalUseCase: DeleteJournalUseCase
) : ViewModel() {

    private val _showNewEntryDialog = MutableStateFlow(false)
    val showNewEntryDialog = _showNewEntryDialog.asStateFlow()

    private val _journals = MutableStateFlow<List<JournalData>>(emptyList())
    val journals: StateFlow<List<JournalData>> get() = _journals

    init {
        loadJournals()
    }

    fun showNewEntryDialog() {
        _showNewEntryDialog.value = true
    }
    fun hideNewEntryDialog() {
        _showNewEntryDialog.value = false
    }

    fun loadJournals() {
        viewModelScope.launch {
            getJournalsUseCase().collect {
                _journals.value = it
            }
        }
    }
    fun deleteJournal(journal: JournalData) {
        viewModelScope.launch {
            deleteJournalUseCase(journal)
        }
    }
    fun onRefresh() {
        _journals.value = emptyList()
        loadJournals()
    }
}