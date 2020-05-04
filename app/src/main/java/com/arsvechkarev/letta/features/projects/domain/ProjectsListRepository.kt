package com.arsvechkarev.letta.features.projects.domain

import android.content.Context
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.directoryProjects
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.utils.hasFilesWithin

class ProjectsListRepository(
  private val context: Context
) {
  
  fun getAllProjects(): List<Project> {
    if (context.hasFilesWithin(directoryProjects)) {
      return listOf(
        Project(context.resources.getDrawable(R.drawable.bg_1, context.theme)),
        Project(context.resources.getDrawable(R.drawable.bg_0_for_png, context.theme)),
        Project(context.resources.getDrawable(R.drawable.bg_1, context.theme)),
        Project(context.resources.getDrawable(R.drawable.bg_1, context.theme)),
        Project(context.resources.getDrawable(R.drawable.bg_2, context.theme)),
        Project(context.resources.getDrawable(R.drawable.bg_2, context.theme)),
        Project(context.resources.getDrawable(R.drawable.bg_3, context.theme))
      )
    }
    return emptyList()
  }
}