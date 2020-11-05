package com.arsvechkarev.letta.features.drawing.presentation

import com.arsvechkarev.letta.core.mvp.MvpView

interface DrawingMvpView : MvpView {
  
  fun onStartUploadingImage()
  
  fun onImageUploaded()
}