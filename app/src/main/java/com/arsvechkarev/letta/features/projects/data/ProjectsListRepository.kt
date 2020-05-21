package com.arsvechkarev.letta.features.projects.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.arsvechkarev.letta.core.assertThat
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.utils.allProjectsDirectory
import com.arsvechkarev.letta.utils.hasProjectFiles
import com.arsvechkarev.letta.views.RoundedCornersDrawable
import timber.log.Timber
import java.io.File

class ProjectsListRepository(
  private val context: Context
) {
  
  fun getAllProjects(): List<Project> {
    if (context.hasProjectFiles()) {
      val list = ArrayList<Project>()
      try {
        val allProjectsDir = context.allProjectsDirectory
        val directories = allProjectsDir.list()
        assertThat(directories != null)
        directories.forEach { filename ->
          val tempFile = File(allProjectsDir, filename)
          val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
          }
          val bitmap = BitmapFactory.decodeFile(tempFile.canonicalPath, options)
          val drawable = RoundedCornersDrawable.ofBitmap(context, bitmap)
          list.add(Project(drawable))
        }
      } catch (e: Throwable) {
        Timber.d(e)
      }
      return list
    }
    return emptyList()
  }
}