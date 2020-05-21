package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.MvpPresenter
import com.arsvechkarev.letta.core.async.AndroidThreader
import com.arsvechkarev.letta.core.async.Threader
import com.arsvechkarev.letta.features.projects.data.ProjectsListRepository

class ProjectsListPresenter(
  private val repository: ProjectsListRepository,
  threader: Threader = AndroidThreader
) : MvpPresenter<ProjectsListView>(threader) {
  
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
}