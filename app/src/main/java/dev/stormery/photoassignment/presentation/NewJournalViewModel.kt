package dev.stormery.photoassignment.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stormery.photoassignment.domain.usecase.AddJournalUseCase
import dev.stormery.photoassignment.presentation.camera.UIEvent
import dev.stormery.photoassignment.presentation.model.JournalData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewJournalViewModel(
    private val addJournalUseCase: AddJournalUseCase,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _journalData = MutableStateFlow(JournalData())
    val journalData = _journalData


    fun onCameraPermissionGranted(){
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.StartVideoCapture)
        }
    }

    fun onPermissionDenied() {
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.ShowPermissionDenied)
        }
    }

    fun onPermanentlyDenied() {
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.OnPermanentlyDenied)
        }
    }

    fun setVideoUri(uri: Uri) {
        viewModelScope.launch {
            _journalData.update {
                it.copy(videoUri = uri)
            }
        }
    }

    fun setDescription(description: String) {
        viewModelScope.launch {
            _journalData.update {
                it.copy(description = description)
            }
        }
    }

    fun setTimestamp(timestamp: Long) {
        viewModelScope.launch {
            _journalData.update {
                it.copy(timestamp = timestamp)
            }
        }
    }
    fun saveJournal(onSavedEntry: () -> Unit) = run {
        viewModelScope.launch {
            addJournalUseCase(_journalData.value.videoUri.toString(), _journalData.value.videoPath, _journalData.value.description, _journalData.value.timestamp)
            resetEntryState()
            onSavedEntry()
        }
    }

    fun resetEntryState() {
        viewModelScope.launch {
            _journalData.update {
                it.copy(
                    videoUri = null,
                    videoPath = "",
                    description = null,
                    timestamp = System.currentTimeMillis()
                )
            }
        }
    }

    fun setVideoPath(videoPath: String) {
        viewModelScope.launch {
            _journalData.update {
                it.copy(videoPath = videoPath)
            }
        }
    }
}