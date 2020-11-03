package com.arsvechkarev.letta.features.projects.list

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.recycler.MultiSelectionAdapter
import com.arsvechkarev.letta.extensions.childImageView
import com.arsvechkarev.letta.extensions.childView
import com.arsvechkarev.letta.extensions.childViewAs
import com.arsvechkarev.letta.extensions.getDimen
import com.arsvechkarev.letta.extensions.i
import com.arsvechkarev.letta.features.projects.list.ProjectsListAdapter.ProjectViewHolder
import com.arsvechkarev.letta.views.Checkmark
import com.arsvechkarev.letta.views.ClickableSquareImage
import com.arsvechkarev.letta.views.drawables.RoundedCornersDrawable

class ProjectsListAdapter(
  private val onProjectClick: (Project) -> Unit
) : MultiSelectionAdapter<Project, ProjectViewHolder>() {
  
  fun addProject(project: Project) {
    (data as ArrayList<Project>).add(project)
    notifyItemInserted(data.lastIndex)
  }
  
  override fun onSwitchingToSelectionMode(layoutManager: LinearLayoutManager) {
    val start = layoutManager.findFirstVisibleItemPosition()
    val end = layoutManager.findLastVisibleItemPosition()
    for (i in start..end) {
      val child = layoutManager.findViewByPosition(i)!!
      val checkmark = child.childViewAs<Checkmark>(Checkmark)
      val image = child.childViewAs<ClickableSquareImage>(Image)
      checkmark.drawBorder = true
      image.scaleOnTouch = false
    }
    notifyItemRangeChanged(end + 1, itemCount - end + 1)
  }
  
  override fun onSwitchingBackFromSelectionMode(layoutManager: LinearLayoutManager) {
    val start = layoutManager.findFirstVisibleItemPosition()
    val end = layoutManager.findLastVisibleItemPosition()
    for (i in start..end) {
      val child = layoutManager.getChildAt(i)!!
      val checkmark = child.childViewAs<Checkmark>(Checkmark)
      val image = child.childViewAs<ClickableSquareImage>(Image)
      checkmark.drawBorder = false
      checkmark.isChecked = false
      image.scaleOnTouch = true
    }
    notifyItemRangeChanged(end + 1, itemCount - end + 1)
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
    val frameLayout = FrameLayout(parent.context).apply {
      val image = ClickableSquareImage(parent.context).apply {
        tag = Image
        scaleOnTouch = true
      }
      val checkmark = Checkmark(parent.context).apply {
        tag = Checkmark
      }
      val imageParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
        val margin = parent.context.getDimen(R.dimen.project_item_image_margin).toInt()
        setMargins(margin, margin, margin, margin)
      }
      addView(image, imageParams)
      val size = parent.context.getDimen(R.dimen.item_bg_image_checkmark_size).toInt()
      val checkmarkParams = LayoutParams(size, size).apply {
        val checkmarkMargin = parent.context.getDimen(R.dimen.project_item_checkmark_margin).i
        setMargins(checkmarkMargin, checkmarkMargin, checkmarkMargin, checkmarkMargin)
      }
      addView(checkmark, checkmarkParams)
    }
    return ProjectViewHolder(
      frameLayout,
      selectedItemPositions,
      isInSelectionModeLambda,
      { position -> onProjectClick(data[position]) }
    )
  }
  
  class ProjectViewHolder(
    itemView: View,
    selectedPositions: ArrayList<Int>,
    isInSelectionMode: () -> Boolean,
    onItemClick: (Int) -> Unit,
  ) : MultiSelectionViewHolder<Project>(
    itemView,
    selectedPositions,
    isInSelectionMode,
    onItemClick
  ) {
    
    override fun viewForClickListener(itemView: View) = itemView.childView(Image)
    
    override fun bindItem(item: Project) {
      val drawable = RoundedCornersDrawable.ofBitmap(itemView.context, item.image)
      itemView.childImageView(Image).setImageDrawable(drawable)
    }
    
    override fun setSelectedWithoutAnimation(itemView: View) {
      itemView.childViewAs<Checkmark>(Checkmark).updateCheckedStateWithoutAnimation(true)
    }
    
    override fun setDisabledWithoutAnimation(itemView: View) {
      itemView.childViewAs<Checkmark>(Checkmark).updateCheckedStateWithoutAnimation(false)
    }
    
    override fun setSelected(itemView: View) {
      itemView.childViewAs<Checkmark>(Checkmark).isChecked = true
    }
    
    override fun setDisabled(itemView: View) {
      itemView.childViewAs<Checkmark>(Checkmark).isChecked = false
    }
    
    override fun goToSelectionModeWithoutAnimation(itemView: View) {
      val checkmark = itemView.childViewAs<Checkmark>(Checkmark)
      checkmark.updateBorderStateWithoutAnimation(true)
    }
    
    override fun switchFromSelectionModeWithoutAnimation(itemView: View) {
      val checkmark = itemView.childViewAs<Checkmark>(Checkmark)
      checkmark.updateBorderStateWithoutAnimation(false)
    }
  }
  
  private companion object {
    
    const val Image = "Image"
    const val Checkmark = "Checkmark"
  }
}