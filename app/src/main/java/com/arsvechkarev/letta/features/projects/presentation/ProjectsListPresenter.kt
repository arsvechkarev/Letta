package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.ProjectsFilesObserver
import com.arsvechkarev.letta.core.async.AndroidThreader
import com.arsvechkarev.letta.core.async.Threader
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.mvp.MvpPresenter
import com.arsvechkarev.letta.extensions.assertThat
import com.arsvechkarev.letta.features.projects.domain.ProjectsListRepository

class ProjectsListPresenter(
  private val repository: ProjectsListRepository,
  threader: Threader = AndroidThreader
) : MvpPresenter<ProjectsListView>(threader), ProjectsFilesObserver.Observer {
  
  var selectionMode = false
    private set
  
  private var currentItemIndex = 0
  
  @Volatile
  private var isLoadingNow = false
  
  private val currentlySelectedPositions = HashSet<Int>()
  private val currentlySelectedProjects = HashSet<Project>()
  
  fun loadProjects() {
    if (isLoadingNow) return
    isLoadingNow = true
    onIoThread {
      if (!repository.hasMoreProjects(currentItemIndex)) {
        updateView {
          projectsAreEmpty()
          isLoadingNow = false
        }
      } else {
        if (currentItemIndex > 0) {
          updateView { onLoadingMoreProjects() }
        }
        val projects = repository.getProjects(currentItemIndex, AMOUNT_PER_LOADING)
        assertThat(projects.isNotEmpty())
        if (currentItemIndex == 0) {
          updateView {
            onLoadedFirstProjects(projects)
            isLoadingNow = false
          }
        } else {
          updateView {
            onLoadedMoreProjects(projects)
            isLoadingNow = false
          }
        }
        currentItemIndex += AMOUNT_PER_LOADING
      }
    }
  }
  
  fun onMoreButtonClicked() {
    if (currentItemIndex > 0) {
      selectionMode = true
      updateView { onSwitchToSelectionMode() }
    }
  }
  
  fun switchBackFromSelectionMode() {
    selectionMode = false
    currentlySelectedProjects.clear()
    updateView { onSwitchBackFromSelectionMode() }
  }
  
  fun onLongClickOnItem() {
    if (!selectionMode) {
      selectionMode = true
      updateView { onSwitchToSelectionModeFromLongClick() }
    }
  }
  
  fun onProjectSelected(position: Int, project: Project) {
    currentlySelectedPositions.add(position)
    currentlySelectedProjects.add(project)
    updateTrashIconState()
  }
  
  fun onProjectUnselected(position: Int, project: Project) {
    currentlySelectedPositions.remove(position)
    currentlySelectedProjects.remove(project)
    updateTrashIconState()
  }
  
  fun onDeleteSelectedProjects() {
    onIoThread {
      if (currentlySelectedProjects.isNotEmpty()) {
        updateView {
          onAskToDeleteProjects(currentlySelectedProjects.size)
        }
      }
    }
  }
  
  fun onConfirmedToDelete() {
    onIoThread {
      repository.deleteProjects(currentlySelectedProjects)
      updateView {
        selectionMode = false
        onConfirmedToDeleteProjects(currentlySelectedProjects)
        currentlySelectedProjects.clear()
        onSwitchBackFromSelectionMode()
      }
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
    currentItemIndex++
    updateView { onProjectCreated(project) }
  }
  
  private fun updateTrashIconState() {
    println("pp: position changed = $currentlySelectedPositions")
    if (currentlySelectedProjects.isEmpty()) {
      updateView { disableTrashIcon() }
    } else {
      updateView { enableTrashIcon() }
    }
  }
  
  companion object {
    
    private const val AMOUNT_PER_LOADING = 30
  }
}