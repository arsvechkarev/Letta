package com.arsvechkarev.letta.features.projects.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.arsvechkarev.letta.LettaApplication
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.MvpFragment
import com.arsvechkarev.letta.core.animateInvisibleAndScale
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.features.projects.data.ProjectsListRepository
import com.arsvechkarev.letta.features.projects.list.ProjectsListAdapter
import com.arsvechkarev.letta.utils.navigator
import kotlinx.android.synthetic.main.fragment_all_projects.buttonNewProject
import kotlinx.android.synthetic.main.fragment_all_projects.projectsLoadingProgressBar
import kotlinx.android.synthetic.main.fragment_all_projects.recyclerAllProjects

class ProjectsListFragment : MvpFragment<ProjectsListView, ProjectsListPresenter>(
  ProjectsListPresenter::class,
  layout = R.layout.fragment_all_projects
), ProjectsListView {
  
  private val adapter = ProjectsListAdapter(onProjectClick = { project ->
    navigator.openExistingProject(project)
  })
  
  override fun createPresenter(): ProjectsListPresenter {
    return ProjectsListPresenter(ProjectsListRepository(LettaApplication.appContext))
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerAllProjects.adapter = adapter
    recyclerAllProjects.layoutManager = GridLayoutManager(requireContext(), 3,
      GridLayoutManager.VERTICAL, false)
    presenter.startLoadingProjects()
    buttonNewProject.setOnClickListener {
      navigator.goToNewProject()
    }
  }
  
  override fun onLoadedProjects(list: List<Project>) {
    adapter.submitList(list)
    projectsLoadingProgressBar.animateInvisibleAndScale()
  }
  
  override fun projectsAreEmpty() {
  
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    recyclerAllProjects.adapter = null
  }
}
