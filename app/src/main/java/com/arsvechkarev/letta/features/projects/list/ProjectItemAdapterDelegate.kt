package com.arsvechkarev.letta.features.projects.list

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.recycler.DelegateViewHolder
import com.arsvechkarev.letta.core.recycler.ListAdapterDelegate
import com.arsvechkarev.letta.core.recycler.MultiSelectionAdapter.MultiSelectionViewHolder
import com.arsvechkarev.letta.extensions.childImageView
import com.arsvechkarev.letta.extensions.childView
import com.arsvechkarev.letta.extensions.childViewAs
import com.arsvechkarev.letta.extensions.getDimen
import com.arsvechkarev.letta.extensions.i
import com.arsvechkarev.letta.views.Checkmark
import com.arsvechkarev.letta.views.ClickableSquareImage
import com.arsvechkarev.letta.views.drawables.RoundedCornersDrawable

class ProjectItemAdapterDelegate(
  private val isInSelectionModeLambda: () -> Boolean,
  private val onProjectItemClick: (Int) -> Unit
) : ListAdapterDelegate<Project>(Project::class) {
  
  private var selectedItemPositions = ArrayList<Int>()
  
  fun setSelectedPositions(selectedItemPositions: ArrayList<Int>) {
    this.selectedItemPositions = selectedItemPositions
  }
  
  override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Project> {
    val frameLayout = FrameLayout(parent.context).apply {
      val image = ClickableSquareImage(parent.context).apply {
        tag = Image
        scaleOnTouch = true
      }
      val checkmark = Checkmark(parent.context).apply {
        tag = Checkmark
      }
      val imageParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT).apply {
        val margin = parent.context.getDimen(R.dimen.project_item_image_margin).toInt()
        setMargins(margin, margin, margin, margin)
      }
      addView(image, imageParams)
      val size = parent.context.getDimen(R.dimen.item_bg_image_checkmark_size).toInt()
      val checkmarkParams = FrameLayout.LayoutParams(size, size).apply {
        val checkmarkMargin = parent.context.getDimen(R.dimen.project_item_checkmark_margin).i
        setMargins(checkmarkMargin, checkmarkMargin, checkmarkMargin, checkmarkMargin)
      }
      addView(checkmark, checkmarkParams)
    }
    return ProjectViewHolder(
      frameLayout,
      selectedItemPositions,
      isInSelectionModeLambda,
      onProjectItemClick
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
      val image = itemView.childViewAs<ClickableSquareImage>(Image)
      val checkmark = itemView.childViewAs<Checkmark>(Checkmark)
      image.scaleOnTouch = false
      checkmark.updateBorderStateWithoutAnimation(true)
    }
    
    override fun switchFromSelectionModeWithoutAnimation(itemView: View) {
      val image = itemView.childViewAs<ClickableSquareImage>(Image)
      val checkmark = itemView.childViewAs<Checkmark>(Checkmark)
      image.scaleOnTouch = true
      checkmark.updateBorderStateWithoutAnimation(false)
    }
  }
  
  companion object {
    
    const val Image = "Image"
    const val Checkmark = "Checkmark"
  }
}