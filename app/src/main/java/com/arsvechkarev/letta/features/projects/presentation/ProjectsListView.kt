package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.MvpView
import com.arsvechkarev.letta.core.model.Project

interface ProjectsListView : MvpView {
  
  fun onLoadedProjects(list: List<Project>)
  
  fun onProjectAdded(project: Project)
  
  fun projectsAreEmpty()
}