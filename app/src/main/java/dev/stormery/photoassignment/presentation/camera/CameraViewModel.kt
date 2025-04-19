package dev.stormery.photoassignment.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<CameraUIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onCameraPermissionGranted(){
        viewModelScope.launch {
            _uiEvent.emit(CameraUIEvent.StartVideoCapture)
        }
    }
}