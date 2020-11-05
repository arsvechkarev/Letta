package com.arsvechkarev.letta.features.drawing.presentation

import android.graphics.Bitmap
import com.arsvechkarev.letta.core.async.AndroidThreader
import com.arsvechkarev.letta.core.async.Threader
import com.arsvechkarev.letta.core.mvp.MvpPresenter
import com.arsvechkarev.letta.features.common.ProjectsRepository

class DrawingPresenter(
  private val repository: ProjectsRepository,
  threader: Threader = AndroidThreader
) : MvpPresenter<DrawingMvpView>(threader) {
  
  fun uploadBitmap(bitmap: Bitmap) {
    updateView { onStartUploadingImage() }
    onIoThread {
      repository.createProject(bitmap)
      updateView { onImageUploaded() }
    }
  }
}