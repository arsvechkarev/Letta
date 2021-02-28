package com.arsvechkarev.letta.features.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.arsvechkarev.letta.core.ProjectsFilesObserver
import com.arsvechkarev.letta.core.model.Project
import java.io.File
import java.io.FileOutputStream

class ProjectFileStorage(
  private val projectsDirectory: File
) {
  
  fun createProject(filename: String, bitmap: Bitmap) {
    projectsDirectory.mkdirs()
    val projectFile = File(projectsDirectory, filename)
    FileOutputStream(projectFile).use {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    ProjectsFilesObserver.notifyProjectCreated(filename)
  }
  
  fun getProject(id: Long, filename: String): Project {
    val projectFile = File(projectsDirectory, filename)
    val options = BitmapFactory.Options().apply {
      inSampleSize = 4
    }
    val bitmap = BitmapFactory.decodeFile(projectFile.canonicalPath, options)
    return Project(id, filename, bitmap)
  }
  
  fun deleteProject(filename: String) {
    File(projectsDirectory, filename).delete()
  }
}