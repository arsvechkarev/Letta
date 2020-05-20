package com.arsvechkarev.letta.core

import com.arsvechkarev.letta.core.model.Project

interface Navigator {
  
  fun goToNewProject()
  
  fun openExistingProject(project: Project)
}