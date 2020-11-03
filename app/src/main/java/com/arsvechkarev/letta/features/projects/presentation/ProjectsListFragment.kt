package com.arsvechkarev.letta.features.projects.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.model.BackgroundType.Color
import com.arsvechkarev.letta.core.model.BackgroundType.DrawableRes
import com.arsvechkarev.letta.core.model.LoadingMoreProjects
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.mvp.MvpFragment
import com.arsvechkarev.letta.core.navigation.navigator
import com.arsvechkarev.letta.extensions.StatusBar
import com.arsvechkarev.letta.extensions.animateInvisibleAndScale
import com.arsvechkarev.letta.extensions.getDimen
import com.arsvechkarev.letta.extensions.paddings
import com.arsvechkarev.letta.features.drawing.presentation.createColorArgs
import com.arsvechkarev.letta.features.drawing.presentation.createDrawableResArgs
import com.arsvechkarev.letta.features.drawing.presentation.createProjectArgs
import com.arsvechkarev.letta.features.projects.di.ProjectsListDi
import com.arsvechkarev.letta.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import com.arsvechkarev.letta.views.behaviors.BottomSheetBehavior.State.SHOWN
import kotlinx.android.synthetic.main.fragment_projects_list.bottomSheetShadowView
import kotlinx.android.synthetic.main.fragment_projects_list.buttonNewProject
import kotlinx.android.synthetic.main.fragment_projects_list.createNewProjectButton
import kotlinx.android.synthetic.main.fragment_projects_list.dialogProjectBackground
import kotlinx.android.synthetic.main.fragment_projects_list.imageMore
import kotlinx.android.synthetic.main.fragment_projects_list.projectsListRoot
import kotlinx.android.synthetic.main.fragment_projects_list.projectsProgressBar
import kotlinx.android.synthetic.main.fragment_projects_list.recyclerAllProjects
import kotlinx.android.synthetic.main.fragment_projects_list.titleAllProjects

class ProjectsListFragment : MvpFragment<ProjectsListView, ProjectsListPresenter>(
  ProjectsListPresenter::class, R.layout.fragment_projects_list
), ProjectsListView {
  
  private val chooseBgContainer by lazy {
    ProjectsListDi.provideChooseBgContainer(this)
  }
  
  private val adapter by lazy {
    ProjectsListDi.provideAdapter(onProjectClick = { project ->
      navigator.openProject(createProjectArgs(
        project, projectsListRoot.width, projectsListRoot.height
      ))
    }, onReadyToLoadFurtherData = {
      presenter.loadProjects()
    })
  }
  
  override fun createPresenter(): ProjectsListPresenter {
    return ProjectsListDi.providePresenter(this)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    StatusBar.setLightStatusBar(requireActivity())
    initViews()
    presenter.loadProjects()
    chooseBgContainer.initialize()
    prepareNewProjectDialog()
  }
  
  override fun onSwitchToSelectionMode() {
    adapter.switchToSelectionMode()
  }
  
  override fun onSwitchBackFromSelectionMode() {
    adapter.switchBackFromSelectionMode()
  }
  
  override fun onLoadedFirstProjects(list: List<Project>) {
    adapter.submitList(list)
    projectsProgressBar.animateInvisibleAndScale()
  }
  
  override fun onLoadingMoreProjects() {
    adapter.addLoadingItem(LoadingMoreProjects)
  }
  
  override fun onLoadedMoreProjects(list: List<Project>) {
    adapter.removeLastAndAdd(list)
  }
  
  override fun onProjectCreated(project: Project) {
    adapter.addItem(project)
  }
  
  override fun projectsAreEmpty() {
    projectsProgressBar.animateInvisibleAndScale()
  }
  
  override fun onBackPressed(): Boolean {
    val bottomSheet = dialogProjectBackground.asBottomSheet
    if (bottomSheet.state == SHOWN) {
      bottomSheet.hide()
      return false
    }
    return true
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    recyclerAllProjects.adapter = null
  }
  
  private fun initViews() {
    recyclerAllProjects.adapter = adapter
    recyclerAllProjects.layoutManager = GridLayoutManager(requireContext(), 3,
      GridLayoutManager.VERTICAL, false)
    val padding = requireContext().getDimen(R.dimen.text_title_padding).toInt()
    titleAllProjects.paddings(
      top = padding,
      start = padding,
      bottom = padding,
    )
    imageMore.setOnClickListener { presenter.onMoreButtonClicked() }
  }
  
  private fun prepareNewProjectDialog() {
    val bottomSheet = dialogProjectBackground.asBottomSheet
    bottomSheet.onShow = { buttonNewProject.allowAnimating = false }
    bottomSheet.onHide = { buttonNewProject.allowAnimating = true }
    buttonNewProject.setOnClickListener { bottomSheet.show() }
    createNewProjectButton.setOnClickListener { openNewProject() }
    bottomSheet.addSlideListener { percentageOpened ->
      val shadowColor = ColorUtils.blendARGB(Colors.Transparent, Colors.Shadow, percentageOpened)
      bottomSheetShadowView.setBackgroundColor(shadowColor)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val endColor = ColorUtils.compositeColors(Colors.Shadow, Colors.Background)
        val statusColor = ColorUtils.blendARGB(Colors.Background, endColor, percentageOpened)
        StatusBar.setStatusBarColor(requireActivity(), statusColor)
      }
    }
    bottomSheet.addSlideListener { percentageOpened ->
      buttonNewProject.alpha = 1 - percentageOpened
      buttonNewProject.translationY = (buttonNewProject.measuredHeight / 2f) * percentageOpened
      buttonNewProject.isClickable = buttonNewProject.alpha != 0f
    }
  }
  
  private fun openNewProject() {
    val width = projectsListRoot.width
    val height = projectsListRoot.height
    val args = when (val type = chooseBgContainer.getBackgroundType()) {
      is Color -> createColorArgs(type.color, width, height)
      is DrawableRes -> createDrawableResArgs(type.drawableRes, width, height)
    }
    navigator.openProject(args)
  }
}