package com.arsvechkarev.letta.features.projects.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.ImageModel
import com.arsvechkarev.letta.core.recycler.SingleSelectionAdapter
import com.arsvechkarev.letta.extensions.inflate
import com.arsvechkarev.letta.features.projects.list.BackgroundImageAdapter.BackgroundImageViewHolder
import com.arsvechkarev.letta.views.RoundedCornersDrawable
import kotlinx.android.synthetic.main.item_bg_image.view.checkmark
import kotlinx.android.synthetic.main.item_bg_image.view.image

class BackgroundImageAdapter(
  data: List<ImageModel>,
  private val onImageSelected: (ImageModel?) -> Unit
) : SingleSelectionAdapter<ImageModel, BackgroundImageViewHolder>(data) {
  
  fun disableSelection() {
    val currentViewHolder = getSelectedView() ?: return
    currentViewHolder.itemView.checkmark.isChecked = false
    selectedPosition = RecyclerView.NO_POSITION
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundImageViewHolder {
    return BackgroundImageViewHolder(parent.inflate(R.layout.item_bg_image))
  }
  
  inner class BackgroundImageViewHolder(itemView: View) : BaseViewHolder<ImageModel>(itemView) {
    
    init {
      itemView.image.setOnClickListener {
        if (selectedPosition == RecyclerView.NO_POSITION) {
          // No view was selected before
          selectedPosition = adapterPosition
          itemView.checkmark.isChecked = true
          onImageSelected(data[selectedPosition])
          return@setOnClickListener
        }
        val previousViewHolder = getSelectedView()
        if (previousViewHolder == this) {
          // Deselecting current view
          onImageSelected(null)
          itemView.checkmark.isChecked = false
          return@setOnClickListener
        }
        if (previousViewHolder == null) {
          // Holder is not found, just updating current view
          itemView.checkmark.isChecked = true
          val prevPosition = selectedPosition
          selectedPosition = adapterPosition
          onImageSelected(data[selectedPosition])
          notifyItemChanged(prevPosition)
        } else {
          // Animating both previous and current view
          previousViewHolder.itemView.checkmark?.isChecked = false
          selectedPosition = adapterPosition
          itemView.checkmark.isChecked = true
          onImageSelected(data[selectedPosition])
        }
      }
    }
    
    override fun bind(item: ImageModel) {
      val drawable = RoundedCornersDrawable.ofResource(itemView.context, item.drawableRes)
      itemView.image.setImageDrawable(drawable)
      val isChecked = selectedPosition == adapterPosition
      itemView.checkmark.updateWithoutAnimation(isChecked)
    }
  }
}