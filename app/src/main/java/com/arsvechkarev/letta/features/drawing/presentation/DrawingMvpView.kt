package com.arsvechkarev.letta.features.drawing.presentation

import com.arsvechkarev.letta.core.MvpView

interface DrawingMvpView : MvpView {
  
  fun onImageStartUploading()
  
  fun onImageUploaded()
  
  fun onImageUploadingError()
}