package com.arsvechkarev.imagesloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

class GlideImagesLoader(private val fragment: Fragment) : ImagesLoader {
  
  private lateinit var url: String
  private var onError: (Throwable) -> Unit = {}
  private var onSuccess: (Drawable) -> Unit = {}
  private var placeholderResId: Int = -1
  
  override fun load(url: String): ImagesLoader {
    this.url = url
    return this
  }
  
  override fun placeholder(placeholderResId: Int): ImagesLoader {
    this.placeholderResId = placeholderResId
    return this
  }
  
  override fun onError(callback: (Throwable) -> Unit): ImagesLoader {
    this.onError = callback
    return this
  }
  
  override fun onSuccess(callback: (Drawable) -> Unit): ImagesLoader {
    this.onSuccess = callback
    return this
  }
  
  override fun into(imageView: ImageView) {
    loadInternal(imageView)
  }
  
  override fun start() {
    loadInternal(null)
  }
  
  private fun loadInternal(imageView: ImageView?) {
    val requestBuilder = Glide.with(fragment)
        .load(url)
        .placeholder(placeholderResId)
        .listener(object : RequestListener<Drawable> {
          override fun onLoadFailed(
            e: GlideException?,
            model: Any,
            target: com.bumptech.glide.request.target.Target<Drawable>,
            isFirstResource: Boolean
          ): Boolean {
            e ?: return false
            onError(e)
            return false
          }
          
          override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: com.bumptech.glide.request.target.Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
          ): Boolean {
            onSuccess(resource)
            return false
          }
        })
    if (imageView == null) {
      requestBuilder.preload()
    } else {
      requestBuilder.into(imageView)
    }
  }
}