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
  
  fun startLoadingProjects() {
    onIoThread {
      val projects = repository.getAllProjects()
      if (projects.isEmpty()) {
        updateView { projectsAreEmpty() }
      } else {
        updateView { onLoadedProjects(projects) }
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
    updateView { onProjectAdded(project) }
  }
}