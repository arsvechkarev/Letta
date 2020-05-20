package com.arsvechkarev.letta.features.drawing.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.arsvechkarev.letta.core.MvpPresenter
import com.arsvechkarev.letta.core.async.AndroidThreader
import com.arsvechkarev.letta.core.async.Threader
import com.arsvechkarev.letta.features.drawing.domain.ImageUploadingRepository
import com.arsvechkarev.letta.views.DrawingView
import timber.log.Timber

class DrawingPresenter(
  private val repository: ImageUploadingRepository,
  threader: Threader = AndroidThreader
) : MvpPresenter<DrawingMvpView>(threader) {
  
  fun uploadBitmap(drawingView: DrawingView, background: View?) {
    updateView { onImageStartUploading() }
    onBackground {
      val bitmap = drawBitmap(drawingView, background)
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
  
  private fun drawBitmap(drawingView: DrawingView, background: View?): Bitmap {
    val bitmap = Bitmap.createBitmap(drawingView.width, drawingView.height,
      Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    background?.draw(canvas)
    drawingView.draw(canvas)
    return bitmap
  }
}