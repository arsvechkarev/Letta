package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.ProjectsFilesObserver
import com.arsvechkarev.letta.core.async.AndroidThreader
import com.arsvechkarev.letta.core.async.Threader
import com.arsvechkarev.letta.core.mvp.MvpPresenter
import com.arsvechkarev.letta.features.projects.domain.ProjectsListRepository

class ProjectsListPresenter(
  private val repository: ProjectsListRepository,
  threader: Threader = AndroidThreader
) : MvpPresenter<ProjectsListView>(threader), ProjectsFilesObserver.Observer {
  
  private var selectionMode = false
  private var currentItemIndex = 0
  
  fun loadProjects() {
    onIoThread {
      if (repository.hasMoreProjects(currentItemIndex)) {
        if (currentItemIndex > 0) {
          updateView { onLoadingMoreProjects() }
        }
        val projects = repository.getProjects(currentItemIndex, AMOUNT_PER_LOADING)
        if (projects.isEmpty()) {
          updateView { projectsAreEmpty() }
        } else {
          if (currentItemIndex == 0) {
            updateView { onLoadedFirstProjects(projects) }
          } else {
            updateView { onLoadedMoreProjects(projects) }
          }
          currentItemIndex += AMOUNT_PER_LOADING
        }
      }
    }
  }
  
  fun onMoreButtonClicked() {
    selectionMode = !selectionMode
    if (selectionMode) {
      updateView { onSwitchToSelectionMode() }
    } else {
      updateView { onSwitchBackFromSelectionMode() }
    }
  }
  
  override fun onViewAttached() {
    ProjectsFilesObserver.setObserver(this)
  }
  
  override fun onViewCleared() {
    ProjectsFilesObserver.clearObserver()
  }
  
  override fun onNewProjectCreated(projectFullPath: String) {
    val project = repository.loadProject(projectFullPath)
    updateView { onProjectCreated(project) }
  }
  
  companion object {
    
    private const val AMOUNT_PER_LOADING = 30
  }
}