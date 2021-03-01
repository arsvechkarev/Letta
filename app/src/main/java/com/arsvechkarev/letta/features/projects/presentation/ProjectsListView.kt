package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.mvp.MvpView

interface ProjectsListView : MvpView {
  
  fun showSwitchToSelectionMode()
  
  fun showSwitchToSelectionModeFromLongClick()
  
  fun enableShareIcon()
  
  fun disableShareIcon()
  
  fun showSwitchBackFromSelectionMode()
  
  fun showSwitchBackFromDeletion(projectsDeleted: Int)
  
  fun showLoadingFirstProjects()
  
  fun showLoadedFirstProjects(list: List<Project>)
  
  fun showLoadingMoreProjects()
  
  fun showLoadedMoreProjects(list: List<Project>)
  
  fun onProjectCreated(project: Project)
  
  fun showProjectsAreEmpty()
  
  fun enableTrashIcon()
  
  fun disableTrashIcon()
  
  fun showAskToDeleteProjects(listSize: Int)
  
  fun showConfirmedToDeleteProjects(list: Collection<Project>)
}