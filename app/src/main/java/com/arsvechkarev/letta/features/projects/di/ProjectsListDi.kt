package com.arsvechkarev.letta.features.projects.di

import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R.id.backgroundImageExample
import com.arsvechkarev.letta.R.id.backgroundImagePalette
import com.arsvechkarev.letta.R.id.backgroundImagesRecyclerView
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.features.common.CommonDi
import com.arsvechkarev.letta.features.projects.list.ProjectsListAdapter
import com.arsvechkarev.letta.features.projects.presentation.ChooseBgContainer
import com.arsvechkarev.letta.features.projects.presentation.ProjectsListFragment
import com.arsvechkarev.letta.features.projects.presentation.ProjectsListPresenter
import com.arsvechkarev.letta.views.BorderImageView
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

object ProjectsListDi {
  
  fun providePresenter(fragment: ProjectsListFragment): ProjectsListPresenter {
    return ProjectsListPresenter(CommonDi.provideProjectRepository())
  }
  
  fun provideChooseBgContainer(fragment: ProjectsListFragment): ChooseBgContainer {
    val view = fragment.requireView()
    val backgroundImageExample = view.findViewById<BorderImageView>(backgroundImageExample)
    val backgroundImagePalette = view.findViewById<GradientPalette>(backgroundImagePalette)
    val backgroundImagesRecyclerView = view.findViewById<RecyclerView>(backgroundImagesRecyclerView)
    return ChooseBgContainer(
      backgroundImageExample,
      backgroundImagePalette,
      backgroundImagesRecyclerView
    )
  }
  
  fun provideAdapter(
    onProjectClick: (Project) -> Unit,
    onReadyToLoadFurtherData: () -> Unit,
    onLongClick: () -> Unit,
    onProjectSelected: (Project) -> Unit,
    onProjectUnselected: (Project) -> Unit,
  ): ProjectsListAdapter {
    return ProjectsListAdapter(
      onProjectClick,
      onReadyToLoadFurtherData,
      onLongClick,
      onProjectSelected,
      onProjectUnselected
    )
  }
}