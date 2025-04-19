package dev.stormery.photoassignment.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainScreenViewModel() : ViewModel() {

    val _showNewEntryDialog = MutableStateFlow(false)
    val showNewEntryDialog = _showNewEntryDialog.asStateFlow()

    fun showNewEntryDialog() {
        _showNewEntryDialog.value = true
    }
    fun hideNewEntryDialog() {
        _showNewEntryDialog.value = false
    }
}