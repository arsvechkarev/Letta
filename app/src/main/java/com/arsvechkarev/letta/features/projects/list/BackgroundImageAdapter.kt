package com.arsvechkarev.letta.features.projects.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.ImageModel
import com.arsvechkarev.letta.extensions.inflate
import com.arsvechkarev.letta.features.projects.list.BackgroundImageAdapter.BackgroundImageViewHolder
import com.arsvechkarev.letta.views.RoundedCornersDrawable
import kotlinx.android.synthetic.main.item_bg_image.view.checkmark
import kotlinx.android.synthetic.main.item_bg_image.view.image

class BackgroundImageAdapter(
  private val data: List<ImageModel>,
  private val onImageSelected: (ImageModel?) -> Unit
) : RecyclerView.Adapter<BackgroundImageViewHolder>() {
  
  private var recyclerView: RecyclerView? = null
  private var currentCheckedImagePosition: Int = 0
  
  fun disableSelection() {
    val currentViewHolder = recyclerView!!.findViewHolderForAdapterPosition(
      currentCheckedImagePosition) as? BackgroundImageViewHolder
    currentViewHolder?.itemView?.checkmark?.isChecked = false
    currentCheckedImagePosition = RecyclerView.NO_POSITION
  }
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = recyclerView
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundImageViewHolder {
    return BackgroundImageViewHolder(parent.inflate(R.layout.item_bg_image))
  }
  
  override fun onBindViewHolder(holder: BackgroundImageViewHolder, position: Int) {
    holder.bind(data[position])
  }
  
  override fun getItemCount() = data.size
  
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = null
  }
  
  inner class BackgroundImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    init {
      itemView.image.setOnClickListener {
        if (currentCheckedImagePosition == RecyclerView.NO_POSITION) {
          // No view was selected before
          currentCheckedImagePosition = adapterPosition
          itemView.checkmark.isChecked = true
          onImageSelected(data[currentCheckedImagePosition])
          return@setOnClickListener
        }
        val previousViewHolder = recyclerView!!.findViewHolderForAdapterPosition(
          currentCheckedImagePosition) as? BackgroundImageViewHolder
        if (previousViewHolder == this) {
          // Deselecting current view
          onImageSelected(null)
          itemView.checkmark.isChecked = false
          return@setOnClickListener
        }
        if (previousViewHolder == null) {
          // Holder is not found, just updating current view
          itemView.checkmark.isChecked = true
          val prevPosition = currentCheckedImagePosition
          currentCheckedImagePosition = adapterPosition
          onImageSelected(data[currentCheckedImagePosition])
          notifyItemChanged(prevPosition)
        } else {
          // Animating both previous and current view
          previousViewHolder.itemView.checkmark?.isChecked = false
          currentCheckedImagePosition = adapterPosition
          itemView.checkmark.isChecked = true
          onImageSelected(data[currentCheckedImagePosition])
        }
      }
    }
    
    fun bind(item: ImageModel) {
      val drawable = RoundedCornersDrawable.ofResource(itemView.context, item.drawableRes)
      itemView.image.setImageDrawable(drawable)
      val isChecked = currentCheckedImagePosition == adapterPosition
      println("www: pos = $adapterPosition, isChecked = $isChecked")
      itemView.checkmark.updateWithoutAnimation(isChecked)
    }
  }
}