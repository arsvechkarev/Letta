package com.arsvechkarev.letta.features.drawing.presentation

import android.graphics.Bitmap
import com.arsvechkarev.letta.core.MvpPresenter
import com.arsvechkarev.letta.core.async.AndroidThreader
import com.arsvechkarev.letta.core.async.Threader
import com.arsvechkarev.letta.features.drawing.data.ImageUploadingRepository
import timber.log.Timber

class DrawingPresenter(
  private val repository: ImageUploadingRepository,
  threader: Threader = AndroidThreader
) : MvpPresenter<DrawingMvpView>(threader) {
  
  fun uploadBitmap(bitmap: Bitmap) {
    updateView { onStartUploadingImage() }
    onIoThread {
      try {
        repository.saveBitmapToGallery(bitmap)
        updateView { onImageUploaded() }
      } catch (e: Throwable) {
        Timber.d(e)
        updateView { onImageUploadingError() }
      }
    }
  }
}