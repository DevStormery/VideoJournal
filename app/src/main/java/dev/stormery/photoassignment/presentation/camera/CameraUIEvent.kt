package dev.stormery.photoassignment.presentation.camera

sealed class CameraUIEvent {
    object StartVideoCapture : CameraUIEvent()
    object ShowPermissionDeniedMessage : CameraUIEvent()
    data class ShowError(val message: String): CameraUIEvent()
}