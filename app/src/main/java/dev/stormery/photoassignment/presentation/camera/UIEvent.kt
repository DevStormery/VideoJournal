package dev.stormery.photoassignment.presentation.camera

sealed class UIEvent {
    object StartVideoCapture : UIEvent()
    object ShowPermissionDenied : UIEvent()
    object OnPermanentlyDenied : UIEvent()
}