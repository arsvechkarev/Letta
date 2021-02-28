package com.arsvechkarev.letta.features.projects.presentation

import android.os.Bundle
import android.view.View
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.model.BackgroundType.Color
import com.arsvechkarev.letta.core.model.BackgroundType.DrawableRes
import com.arsvechkarev.letta.core.model.LoadingMoreProjects
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.mvp.MvpFragment
import com.arsvechkarev.letta.core.navigation.navigator
import com.arsvechkarev.letta.core.recycler.CallbackType
import com.arsvechkarev.letta.extensions.StatusBar
import com.arsvechkarev.letta.extensions.animateInvisibleAndScale
import com.arsvechkarev.letta.extensions.animateLoosen
import com.arsvechkarev.letta.extensions.animateSquash
import com.arsvechkarev.letta.extensions.getDimen
import com.arsvechkarev.letta.extensions.gone
import com.arsvechkarev.letta.extensions.invisible
import com.arsvechkarev.letta.extensions.isNotVisible
import com.arsvechkarev.letta.extensions.paddings
import com.arsvechkarev.letta.extensions.visible
import com.arsvechkarev.letta.features.drawing.presentation.createColorArgs
import com.arsvechkarev.letta.features.drawing.presentation.createDrawableResArgs
import com.arsvechkarev.letta.features.drawing.presentation.createProjectArgs
import com.arsvechkarev.letta.features.projects.di.ProjectsListDi
import com.arsvechkarev.letta.views.SafeGridLayoutManager
import com.arsvechkarev.letta.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import com.arsvechkarev.letta.views.behaviors.BottomSheetBehavior.State.SHOWN
import com.arsvechkarev.letta.views.behaviors.HeaderBehavior.Companion.asHeader
import kotlinx.android.synthetic.main.fragment_projects_list.bottomSheetProjectBackground
import kotlinx.android.synthetic.main.fragment_projects_list.bottomSheetShadowView
import kotlinx.android.synthetic.main.fragment_projects_list.buttonNewProject
import kotlinx.android.synthetic.main.fragment_projects_list.createNewProjectButton
import kotlinx.android.synthetic.main.fragment_projects_list.header
import kotlinx.android.synthetic.main.fragment_projects_list.imageBackFromSelectionMode
import kotlinx.android.synthetic.main.fragment_projects_list.imageMore
import kotlinx.android.synthetic.main.fragment_projects_list.imageShare
import kotlinx.android.synthetic.main.fragment_projects_list.imageTrash
import kotlinx.android.synthetic.main.fragment_projects_list.layoutNoProjects
import kotlinx.android.synthetic.main.fragment_projects_list.projectsDialogConfirmDelete
import kotlinx.android.synthetic.main.fragment_projects_list.projectsHeaderDialogConfirmDelete
import kotlinx.android.synthetic.main.fragment_projects_list.projectsListRoot
import kotlinx.android.synthetic.main.fragment_projects_list.projectsProgressBar
import kotlinx.android.synthetic.main.fragment_projects_list.projectsTextDialogCancel
import kotlinx.android.synthetic.main.fragment_projects_list.projectsTextDialogDelete
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
    }, onLongClick = {
      presenter.onItemLongClick()
    }, onProjectSelected = { project ->
      presenter.onProjectSelected(project)
    }, onProjectUnselected = { project ->
      presenter.onProjectUnselected(project)
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
    prepareConfirmDeletionDialog()
  }
  
  override fun showSwitchToSelectionMode() {
    adapter.switchToSelectionMode()
    animateTransitionToSelectionMode()
    imageTrash.isEnabled = false
  }
  
  override fun showSwitchToSelectionModeFromLongClick() {
    adapter.switchToSelectionMode()
    animateTransitionToSelectionMode()
    imageTrash.isEnabled = true
  }
  
  override fun showShareIcon() {
    if (imageShare.isNotVisible) {
      imageShare.animateLoosen()
      imageShare.visible()
    }
  }
  
  override fun hideShareIcon() {
    imageShare.animateSquash(andThen = { imageShare.gone() })
  }
  
  override fun showSwitchBackFromSelectionMode() {
    adapter.switchBackFromSelectionMode()
    animateTransitionFromSelectionMode()
  }
  
  override fun showSwitchBackFromDeletion(projectsDeleted: Int) {
    adapter.switchBackFromSelectionMode()
    animateTransitionFromSelectionMode(
      showMoreIcon = projectsDeleted != adapter.itemCount
    )
  }
  
  override fun showLoadingFirstProjects() {
    layoutNoProjects.invisible()
    projectsProgressBar.visible()
  }
  
  override fun showLoadedFirstProjects(list: List<Project>) {
    adapter.submitList(list, CallbackType.APPENDED_LIST)
    projectsProgressBar.animateInvisibleAndScale()
    if (imageMore.visibility != View.VISIBLE) {
      imageMore.animateLoosen()
    }
  }
  
  override fun showLoadingMoreProjects() {
    adapter.addLoadingItem(LoadingMoreProjects)
  }
  
  override fun showLoadedMoreProjects(list: List<Project>) {
    adapter.removeLastAndAdd(list)
  }
  
  override fun onProjectCreated(project: Project) {
    layoutNoProjects.invisible()
    if (imageMore.visibility != View.VISIBLE) {
      imageMore.animateLoosen()
    }
    adapter.addItem(project, 0)
  }
  
  override fun showProjectsAreEmpty() {
    projectsProgressBar.invisible()
    layoutNoProjects.visible()
  }
  
  override fun enableTrashIcon() {
    imageTrash.isEnabled = true
  }
  
  override fun disableTrashIcon() {
    imageTrash.isEnabled = false
  }
  
  override fun showAskToDeleteProjects(listSize: Int) {
    projectsDialogConfirmDelete.show()
    projectsHeaderDialogConfirmDelete.text = getString(
      R.string.text_are_you_sure_to_delete_projects, listSize
    )
  }
  
  override fun showConfirmedToDeleteProjects(list: Collection<Project>) {
    projectsDialogConfirmDelete.hide()
    adapter.deleteItems(list)
  }
  
  override fun onBackPressed(): Boolean {
    val bottomSheet = bottomSheetProjectBackground.asBottomSheet
    if (bottomSheet.state == SHOWN) {
      bottomSheet.hide()
      return false
    }
    if (presenter.selectionMode) {
      presenter.switchBackFromSelectionMode()
      return false
    }
    return true
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    recyclerAllProjects.adapter = null
  }
  
  private fun initViews() {
    imageShare.setOnClickListener { presenter.shareProject() }
    imageTrash.isEnabled = false
    header.asHeader.isScrollable = false
    recyclerAllProjects.adapter = adapter
    val layoutManager = SafeGridLayoutManager(adapter, requireContext(), 3)
    recyclerAllProjects.layoutManager = layoutManager
    val padding = requireContext().getDimen(R.dimen.text_title_padding).toInt()
    titleAllProjects.paddings(
      top = padding,
      start = padding,
      bottom = padding,
    )
    imageMore.setOnClickListener { presenter.onMoreButtonClicked() }
    imageBackFromSelectionMode.setOnClickListener { presenter.switchBackFromSelectionMode() }
    imageTrash.setOnClickListener { presenter.onDeleteSelectedProjects() }
    projectsTextDialogDelete.setOnClickListener { presenter.onConfirmedToDelete() }
    projectsTextDialogCancel.setOnClickListener { projectsDialogConfirmDelete.hide() }
  }
  
  private fun animateTransitionToSelectionMode() {
    buttonNewProject.animate(down = true)
    buttonNewProject.allowAnimating = false
    imageMore.animateSquash(andThen = { imageMore.gone() })
    titleAllProjects.animateSquash(andThen = { imageMore.gone() })
    imageBackFromSelectionMode.animateLoosen()
    imageTrash.animateLoosen()
    imageTrash.visible()
  }
  
  private fun animateTransitionFromSelectionMode(showMoreIcon: Boolean = true) {
    imageTrash.isEnabled = true
    buttonNewProject.allowAnimating = true
    buttonNewProject.animate(down = false)
    imageShare.animateSquash(andThen = { imageShare.gone() })
    imageTrash.animateSquash(andThen = { imageTrash.gone() })
    imageBackFromSelectionMode.animateSquash(andThen = {
      imageBackFromSelectionMode.gone()
    })
    titleAllProjects.animateLoosen()
    if (showMoreIcon) {
      imageMore.animateLoosen()
      imageMore.visible()
    }
  }
  
  private fun prepareNewProjectDialog() {
    val bottomSheet = bottomSheetProjectBackground.asBottomSheet
    bottomSheet.onShow = { buttonNewProject.allowAnimating = false }
    bottomSheet.onHide = { buttonNewProject.allowAnimating = true }
    buttonNewProject.setOnClickListener { bottomSheet.show() }
    createNewProjectButton.setOnClickListener { openNewProject() }
    bottomSheet.addSlideListener { percentageOpened ->
      val shadowColor = ColorUtils.blendARGB(Colors.Transparent, Colors.Shadow, percentageOpened)
      bottomSheetShadowView.setBackgroundColor(shadowColor)
      StatusBar.applyShadow(requireActivity(), percentageOpened)
    }
    bottomSheet.addSlideListener { percentageOpened ->
      buttonNewProject.alpha = 1f - percentageOpened
      buttonNewProject.translationY = (buttonNewProject.measuredHeight / 2f) * percentageOpened
      buttonNewProject.isClickable = buttonNewProject.alpha != 0f
    }
  }
  
  private fun prepareConfirmDeletionDialog() {
    projectsDialogConfirmDelete.onShadowFractionChangedListener = { percentage ->
      StatusBar.applyShadow(requireActivity(), percentage)
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
    val duration = requireContext().resources.getInteger(R.integer.duration_fragment_transition)
    requireView().postDelayed({
      bottomSheetShadowView.setBackgroundColor(Colors.Transparent)
      buttonNewProject.translationY = 0f
      buttonNewProject.alpha = 1f
      buttonNewProject.isClickable = true
      bottomSheetProjectBackground.asBottomSheet.hideWithoutAnimation()
    }, (duration * 1.2f).toLong())
  }
}