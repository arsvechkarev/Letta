package com.arsvechkarev.letta.photoslist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Image
import com.arsvechkarev.letta.utils.inflate
import kotlinx.android.synthetic.main.item_image.view.image

class PhotosAdapter(
  private val imagesLoader: ImagesLoader,
  private val clickListener: (Image) -> Unit = {}
) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {
  
  private var data: List<Image> = ArrayList()
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
    return PhotoViewHolder(parent.inflate(R.layout.item_image))
  }
  
  override fun getItemCount() = data.size
  
  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
    holder.bind(data[position])
  }
  
  fun submitList(data: List<Image>) {
    this.data = data
    notifyDataSetChanged()
  }
  
  inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    init {
      itemView.setOnClickListener { clickListener(data[adapterPosition]) }
    }
    
    fun bind(image: Image) {
      // TODO (2/3/2020): Create placeholder
      imagesLoader.loadImage(image.url, itemView.image, R.drawable.bg_round)
    }
  }
}