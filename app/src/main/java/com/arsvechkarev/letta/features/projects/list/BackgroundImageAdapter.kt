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
    val currentViewHolder = getSelectedViewHolder() ?: return
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
        val previousViewHolder = getSelectedViewHolder()
        if (previousViewHolder == this) {
          // Deselecting current view
          onImageSelected(null)
          selectedPosition = RecyclerView.NO_POSITION
          itemView.checkmark.isChecked = false
          return@setOnClickListener
        }
        if (previousViewHolder == null) {
          // Holder is not found, just updating current view
          notifyItemChanged(selectedPosition)
        } else {
          // Animating both previous and current view
          previousViewHolder.itemView.checkmark?.isChecked = false
        }
        itemView.checkmark.isChecked = true
        selectedPosition = adapterPosition
        onImageSelected(data[selectedPosition])
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