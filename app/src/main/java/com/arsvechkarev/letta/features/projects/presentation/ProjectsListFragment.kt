package com.arsvechkarev.letta.features.projects.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.MvpFragment
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.features.projects.domain.ProjectsListRepository
import com.arsvechkarev.letta.features.projects.list.ProjectsListAdapter
import kotlinx.android.synthetic.main.fragment_all_projects.recyclerAllProjects

class ProjectsListFragment : MvpFragment<ProjectsListView, ProjectsListPresenter>(
  ProjectsListPresenter::class,
  layout = R.layout.fragment_all_projects
), ProjectsListView {
  
  private val adapter = ProjectsListAdapter()
  
  override fun createPresenter(): ProjectsListPresenter {
    return ProjectsListPresenter(ProjectsListRepository(requireContext()))
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerAllProjects.adapter = adapter
    recyclerAllProjects.layoutManager = GridLayoutManager(requireContext(), 3,
      GridLayoutManager.VERTICAL, false)
    presenter.startLoadingProjects()
  }
  
  override fun onLoadedProjects(list: List<Project>) {
    adapter.submitList(list)
  }
  
  override fun projectsAreEmpty() {
  
  }
}
