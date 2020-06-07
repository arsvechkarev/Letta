package com.arsvechkarev.letta.features.drawing.presentation

import com.arsvechkarev.letta.core.MvpView

interface DrawingMvpView : MvpView {
  
  fun onStartUploadingImage()
  
  fun onImageUploaded()
  
  fun onImageUploadingError()
}