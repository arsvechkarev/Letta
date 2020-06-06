package com.arsvechkarev.letta.features.projects.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.arsvechkarev.letta.LettaApplication
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.MvpFragment
import com.arsvechkarev.letta.core.animateInvisibleAndScale
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.features.projects.data.ProjectsListRepository
import com.arsvechkarev.letta.features.projects.list.ProjectsListAdapter
import com.arsvechkarev.letta.utils.behavior
import com.arsvechkarev.letta.utils.lerpColor
import com.arsvechkarev.letta.utils.navigator
import com.arsvechkarev.letta.views.behaviors.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_projects_list.backgroundImageExample
import kotlinx.android.synthetic.main.fragment_projects_list.backgroundImagePalette
import kotlinx.android.synthetic.main.fragment_projects_list.backgroundImagesRecyclerView
import kotlinx.android.synthetic.main.fragment_projects_list.bottomSheetShadowView
import kotlinx.android.synthetic.main.fragment_projects_list.buttonNewProject
import kotlinx.android.synthetic.main.fragment_projects_list.createNewProjectButton
import kotlinx.android.synthetic.main.fragment_projects_list.dialogProjectBackground
import kotlinx.android.synthetic.main.fragment_projects_list.projectsLoadingProgressBar
import kotlinx.android.synthetic.main.fragment_projects_list.recyclerAllProjects

class ProjectsListFragment : MvpFragment<ProjectsListView, ProjectsListPresenter>(
  ProjectsListPresenter::class,
  layout = R.layout.fragment_projects_list
), ProjectsListView {
  
  private val adapter = ProjectsListAdapter(onProjectClick = { project ->
    navigator.openExistingProject(project)
  })
  
  private lateinit var chooseBgContainer: ChooseBgContainer
  
  override fun createPresenter(): ProjectsListPresenter {
    return ProjectsListPresenter(ProjectsListRepository(LettaApplication.appContext))
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerAllProjects.adapter = adapter
    recyclerAllProjects.layoutManager = GridLayoutManager(requireContext(), 3,
      GridLayoutManager.VERTICAL, false)
    presenter.startLoadingProjects()
    prepareBehavior()
    createNewProjectButton.setOnClickListener {
      navigator.goToNewProject()
    }
    chooseBgContainer = ChooseBgContainer(backgroundImageExample, backgroundImagePalette,
      backgroundImagesRecyclerView)
  }
  
  private fun prepareBehavior() {
    val behavior = dialogProjectBackground.behavior<BottomSheetBehavior<*>>()
    behavior.addSlideListener { slidePercent ->
      val color = lerpColor(0, Colors.COLOR_SHADOW, slidePercent)
      bottomSheetShadowView.setBackgroundColor(color)
    }
    behavior.addSlideListener { slidePercent ->
      buttonNewProject.alpha = 1 - slidePercent
      buttonNewProject.translationY = (buttonNewProject.height / 2f) * slidePercent
    }
    buttonNewProject.setOnClickListener {
      if (behavior.isShown) {
        behavior.hide()
      } else {
        behavior.show()
      }
    }
  }
  
  override fun onLoadedProjects(list: List<Project>) {
    adapter.submitList(list)
    projectsLoadingProgressBar.animateInvisibleAndScale()
  }
  
  override fun projectsAreEmpty() {
    projectsLoadingProgressBar.animateInvisibleAndScale()
  }
  
  override fun onBackPressed(): Boolean {
    val behavior = dialogProjectBackground.behavior<BottomSheetBehavior<*>>()
    if (behavior.isShown) {
      behavior.hide()
      return false
    }
    return true
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    recyclerAllProjects.adapter = null
  }
}
