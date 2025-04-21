package dev.stormery.photoassignment.presentation

import androidx.lifecycle.ViewModel
import dev.stormery.photoassignment.domain.usecase.GetJournalsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainScreenViewModel(
    getJournalsUseCase: GetJournalsUseCase
) : ViewModel() {

    private val _showNewEntryDialog = MutableStateFlow(false)
    val showNewEntryDialog = _showNewEntryDialog.asStateFlow()

    val journals = getJournalsUseCase()

    fun showNewEntryDialog() {
        _showNewEntryDialog.value = true
    }
    fun hideNewEntryDialog() {
        _showNewEntryDialog.value = false
    }
}