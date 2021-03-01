package com.arsvechkarev.letta.features.projects.presentation

import com.arsvechkarev.letta.core.LOADING_DELAY
import com.arsvechkarev.letta.core.ProjectsFilesObserver
import com.arsvechkarev.letta.core.Sharer
import com.arsvechkarev.letta.core.async.AndroidThreader
import com.arsvechkarev.letta.core.async.Threader
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.mvp.MvpPresenter
import com.arsvechkarev.letta.extensions.assertThat
import com.arsvechkarev.letta.features.common.ProjectsRepository

class ProjectsListPresenter(
  private val repository: ProjectsRepository,
  private val sharer: Sharer,
  threader: Threader = AndroidThreader
) : MvpPresenter<ProjectsListView>(threader), ProjectsFilesObserver.Observer {
  
  var selectionMode = false
    private set
  
  private var currentItemIndex = 0
  
  @Volatile
  private var isLoadingNow = false
  
  private val currentlySelectedProjects = HashSet<Project>()
  
  fun loadProjects() {
    if (isLoadingNow) return
    isLoadingNow = true
    if (currentItemIndex == 0) {
      updateView { showLoadingFirstProjects() }
    }
    onIoThread {
      if (!repository.hasMoreProjects(currentItemIndex)) {
        updateView {
          showProjectsAreEmpty()
          isLoadingNow = false
        }
      } else {
        if (currentItemIndex > 0) {
          updateView { showLoadingMoreProjects() }
        }
        Thread.sleep(LOADING_DELAY)
        val projects = repository.getProjects(currentItemIndex, AMOUNT_PER_LOADING)
        assertThat(projects.isNotEmpty())
        if (currentItemIndex == 0) {
          updateView {
            showLoadedFirstProjects(projects)
            isLoadingNow = false
          }
        } else {
          updateView {
            showLoadedMoreProjects(projects)
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
      updateView { showSwitchToSelectionMode() }
    }
  }
  
  fun onItemLongClick() {
    if (!selectionMode) {
      selectionMode = true
      updateView { showSwitchToSelectionModeFromLongClick() }
    }
  }
  
  fun shareProject() {
    assertThat(currentlySelectedProjects.isNotEmpty())
    sharer.share(currentlySelectedProjects)
  }
  
  fun switchBackFromSelectionMode() {
    selectionMode = false
    currentlySelectedProjects.clear()
    updateView { showSwitchBackFromSelectionMode() }
  }
  
  fun onProjectSelected(project: Project) {
    currentlySelectedProjects.add(project)
    updateTrashIconState()
    updateShareIconState()
  }
  
  fun onProjectUnselected(project: Project) {
    currentlySelectedProjects.remove(project)
    updateTrashIconState()
    updateShareIconState()
  }
  
  fun onDeleteSelectedProjects() {
    onIoThread {
      if (currentlySelectedProjects.isNotEmpty()) {
        updateView {
          showAskToDeleteProjects(currentlySelectedProjects.size)
        }
      }
    }
  }
  
  fun onConfirmedToDelete() {
    onIoThread {
      repository.deleteProjects(currentlySelectedProjects)
      if (repository.getNumberOfProjects() == 0) {
        updateView { showProjectsAreEmpty() }
      }
      updateView {
        selectionMode = false
        showSwitchBackFromDeletion(currentlySelectedProjects.size)
        showConfirmedToDeleteProjects(currentlySelectedProjects)
        currentlySelectedProjects.clear()
      }
    }
  }
  
  override fun onViewAttached() {
    ProjectsFilesObserver.setObserver(this)
  }
  
  override fun onViewCleared() {
    ProjectsFilesObserver.clearObserver()
  }
  
  override fun onNewProjectCreated(filename: String) {
    val project = repository.getLatestProject(filename)
    currentItemIndex++
    updateView { onProjectCreated(project) }
  }
  
  private fun updateTrashIconState() {
    if (currentlySelectedProjects.isEmpty()) {
      updateView { disableTrashIcon() }
    } else {
      updateView { enableTrashIcon() }
    }
  }
  
  private fun updateShareIconState() {
    if (currentlySelectedProjects.isEmpty()) {
      updateView { disableShareIcon() }
    } else {
      updateView { enableShareIcon() }
    }
  }
  
  companion object {
    
    private const val AMOUNT_PER_LOADING = 30
  }
}