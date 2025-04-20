package dev.stormery.photoassignment.presentation.camera

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stormery.photoassignment.core.CameraHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<CameraUIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri = _videoUri

    fun onCameraPermissionGranted(){
        viewModelScope.launch {
            _uiEvent.emit(CameraUIEvent.StartVideoCapture)
        }
    }

    fun onCameraPermissionDenied() {
        viewModelScope.launch {
            _uiEvent.emit(CameraUIEvent.ShowPermissionDeniedMessage)
        }
    }

    fun setVideoUri(uri: Uri) {
        _videoUri.value = uri
    }
}