package com.arsvechkarev.letta.features.projects.domain

import android.content.Context
import android.graphics.BitmapFactory
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.extensions.allProjectsDirectory
import com.arsvechkarev.letta.extensions.hasProjectFiles
import timber.log.Timber
import java.io.File

class ProjectsListRepository(
  private val context: Context
) {
  
  fun hasMoreProjects(currentItemIndex: Int): Boolean {
    if (context.hasProjectFiles()) {
      val files = context.allProjectsDirectory.list()!!
      return currentItemIndex < files.lastIndex
    }
    return false
  }
  
  fun getProjects(fromIndex: Int, amount: Int): List<Project> {
    if (context.hasProjectFiles()) {
      val list = ArrayList<Project>()
      try {
        val allProjectsDir = context.allProjectsDirectory
        val directories = allProjectsDir.list()!!
        val start = minOf(fromIndex, directories.lastIndex)
        val end = minOf(fromIndex + amount, directories.lastIndex)
        for (i in start until end) {
          val filename = directories[i]
          val tempFile = File(allProjectsDir, filename)
          val options = BitmapFactory.Options().apply {
            inSampleSize = 4
          }
          val bitmap = BitmapFactory.decodeFile(tempFile.canonicalPath, options)
          list.add(Project(tempFile.canonicalPath, bitmap))
        }
      } catch (e: Throwable) {
        Timber.d(e)
      }
      return list
    }
    return emptyList()
  }
  
  fun loadProject(projectFullPath: String): Project {
    val tempFile = File(projectFullPath)
    val options = BitmapFactory.Options().apply {
      inSampleSize = 4
    }
    val bitmap = BitmapFactory.decodeFile(tempFile.canonicalPath, options)
    return Project(tempFile.canonicalPath, bitmap)
  }
}