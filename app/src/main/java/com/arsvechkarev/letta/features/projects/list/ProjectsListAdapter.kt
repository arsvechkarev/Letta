package com.arsvechkarev.letta.features.projects.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.features.projects.list.ProjectsListAdapter.ProjectViewHolder
import com.arsvechkarev.letta.utils.inflate
import kotlinx.android.synthetic.main.item_project.view.projectImage

class ProjectsListAdapter : RecyclerView.Adapter<ProjectViewHolder>() {
  
  private var data: List<Project> = ArrayList()
  
  fun submitList(data: List<Project>) {
    this.data = data
    notifyDataSetChanged()
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
    return ProjectViewHolder(parent.inflate(R.layout.item_project))
  }
  
  override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
    holder.bind(data[position])
  }
  
  override fun getItemCount() = data.size
  
  class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(project: Project) {
      itemView.projectImage.setImageDrawable(project.image)
    }
  }
}