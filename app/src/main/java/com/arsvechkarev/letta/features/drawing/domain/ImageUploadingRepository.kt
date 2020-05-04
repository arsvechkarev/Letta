package com.arsvechkarev.letta.features.drawing.domain

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.arsvechkarev.letta.core.directoryProjects
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageUploadingRepository(
  private val context: Context
) {
  
  fun saveBitmapToGallery(bitmap: Bitmap) {
    val baseDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val directory = File(baseDir, directoryProjects)
    if (!directory.exists()) {
      directory.mkdirs()
    }
    val dateFormat = SimpleDateFormat("yyyy_MM_dd-HH_mm_ss", Locale.getDefault())
    val timestamp = dateFormat.format(Date())
    val filename = "Project_$timestamp.jpg"
    val file = File(directory, filename)
    Timber.d("Saving image, file = ${file.path}")
    FileOutputStream(file).use {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
    }
  }
}