package com.arsvechkarev.letta.features.drawing.list

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.core.model.Image
import com.arsvechkarev.letta.features.drawing.list.BackgroundImageAdapter.BackgroundImageViewHolder
import com.arsvechkarev.letta.utils.dpInt

class BackgroundImageAdapter(private val data: List<Image>) :
  RecyclerView.Adapter<BackgroundImageViewHolder>() {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundImageViewHolder {
    val imageView = ImageView(parent.context)
    val layoutParams = RecyclerView.LayoutParams(50.dpInt, 50.dpInt)
    layoutParams.setMargins(8.dpInt, 8.dpInt, 8.dpInt, 8.dpInt)
    imageView.layoutParams = layoutParams
    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
    return BackgroundImageViewHolder(imageView)
  }
  
  override fun onBindViewHolder(holder: BackgroundImageViewHolder, position: Int) {
    holder.bind(data[position])
  }
  
  override fun getItemCount() = data.size
  
  class BackgroundImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(item: Image) {
      (itemView as ImageView).setImageDrawable(
        itemView.resources.getDrawable(item.drawableRes, itemView.context.theme))
    }
  }
}