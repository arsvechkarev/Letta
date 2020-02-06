package com.arsvechkarev.letta.media

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.imagesloader.ImagesLoader
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Image
import com.arsvechkarev.letta.media.common.ImagesViewModel
import com.arsvechkarev.letta.utils.inflate
import kotlinx.android.synthetic.main.item_image.view.imageInRecycler

class ImagesListAdapter(
  private val imagesViewModel: ImagesViewModel,
  private val imagesLoader: ImagesLoader,
  private val clickListener: (View, Int) -> Unit,
  private val loadingCompletedListener: (Int) -> Unit
) : RecyclerView.Adapter<ImagesListAdapter.PhotoViewHolder>() {
  
  private var data: List<Image> = ArrayList()
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
    return PhotoViewHolder(parent.inflate(R.layout.item_image))
  }
  
  override fun getItemCount() = data.size
  
  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
    holder.bind(data[position], position)
  }
  
  fun submitList(data: List<Image>) {
    this.data = data
    notifyDataSetChanged()
  }
  
  inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    init {
      itemView.setOnClickListener { clickListener(itemView, adapterPosition) }
    }
    
    fun bind(image: Image, position: Int) {
      itemView.transitionName = imagesViewModel.data[position].url
      // TODO (2/3/2020): Create placeholder
      imagesLoader.load(image.url)
          .placeholder(R.drawable.placeholder)
          .onSuccess { loadingCompletedListener(adapterPosition) }
          .onError { loadingCompletedListener(adapterPosition) }
          .into(itemView.imageInRecycler)
    }
  }
}