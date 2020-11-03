package com.arsvechkarev.letta.features.drawing.domain

import android.content.Context
import android.graphics.Bitmap
import com.arsvechkarev.letta.core.ProjectsFilesObserver
import com.arsvechkarev.letta.extensions.allProjectsDirectory
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SavingProjectRepository(
  private val context: Context
) {
  
  fun saveBitmapToGallery(bitmap: Bitmap) {
    val directory = context.allProjectsDirectory
    directory.mkdirs()
    val dateFormat = SimpleDateFormat("yyyy_MM_dd-HH_mm_ss", Locale.getDefault())
    repeat(10) {
      val timestamp = dateFormat.format(Date()) + UUID.randomUUID().toString()
      val projectFile = File(directory, "Project_$timestamp.png")
      Timber.d("Saving project, file = ${projectFile.path}")
      FileOutputStream(projectFile).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
      }
      ProjectsFilesObserver.notifyProjectCreated(projectFile.canonicalPath)
    }
  }
}