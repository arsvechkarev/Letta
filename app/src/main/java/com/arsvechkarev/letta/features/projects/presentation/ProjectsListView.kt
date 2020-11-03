package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.mvp.MvpView

interface ProjectsListView : MvpView {
  
  fun onSwitchToSelectionMode()
  
  fun onSwitchBackFromSelectionMode()
  
  fun onLoadedFirstProjects(list: List<Project>)
  
  fun onLoadingMoreProjects()
  
  fun onLoadedMoreProjects(list: List<Project>)
  
  fun onProjectCreated(project: Project)
  
  fun projectsAreEmpty()
}