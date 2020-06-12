package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.mvp.MvpView

interface ProjectsListView : MvpView {
  
  fun onLoadedProjects(list: List<Project>)
  
  fun onProjectAdded(project: Project)
  
  fun projectsAreEmpty()
}