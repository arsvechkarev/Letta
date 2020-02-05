package com.arsvechkarev.letta.media

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Image
import com.arsvechkarev.letta.media.common.ImagesViewModel
import com.arsvechkarev.letta.utils.inflate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_image.view.imageInRecycler

class ImagesListAdapter(
  private val imagesViewModel: ImagesViewModel,
  private val fragment: Fragment,
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
      Glide.with(fragment)
        .load(image.url)
        .placeholder(R.drawable.placeholder)
        .listener(object : RequestListener<Drawable> {
          override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
          ): Boolean {
            loadingCompletedListener(adapterPosition)
            return false
          }
          
          override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
          ): Boolean {
            loadingCompletedListener(adapterPosition)
            return false
          }
        }).into(itemView.imageInRecycler)
    }
  }
}