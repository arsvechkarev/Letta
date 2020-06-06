package com.arsvechkarev.letta.features.projects.list

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.features.projects.list.ProjectsListAdapter.ProjectViewHolder
import com.arsvechkarev.letta.utils.getDimen
import com.arsvechkarev.letta.views.ClickableSquareImage

class ProjectsListAdapter(
  private val onProjectClick: (Project) -> Unit
) : RecyclerView.Adapter<ProjectViewHolder>() {
  
  private var data: List<Project> = ArrayList()
  
  fun submitList(data: List<Project>) {
    this.data = data
    notifyDataSetChanged()
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
    val image = ClickableSquareImage(parent.context)
    val params = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
    val margin = parent.context.getDimen(R.dimen.item_bg_image_margin).toInt()
    params.setMargins(margin, margin, margin, margin)
    image.layoutParams = params
    return ProjectViewHolder(data, onProjectClick, image)
  }
  
  override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
    holder.bind(data[position])
  }
  
  override fun getItemCount() = data.size
  
  class ProjectViewHolder(
    data: List<Project>,
    onProjectClick: (Project) -> Unit,
    itemView: View
  ) : RecyclerView.ViewHolder(itemView) {
    
    init {
      itemView.setOnClickListener {
        onProjectClick.invoke(data[adapterPosition])
      }
    }
    
    fun bind(project: Project) {
      (itemView as ImageView).setImageDrawable(project.image)
    }
  }
}