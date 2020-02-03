package com.arsvechkarev.imagesloader

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target as GlideTarget

class GlideImagesLoader(private val fragment: Fragment) : ImagesLoader {
  
  private lateinit var requestBuilder: RequestBuilder<Drawable>
  
  override fun load(url: String): GlideImagesLoader {
    requestBuilder = Glide.with(fragment).load(url)
    return this
  }
  
  @SuppressLint("CheckResult")
  override fun onError(callback: (Throwable) -> Unit): GlideImagesLoader {
    requestBuilder.addListener(object : RequestListener<Drawable> {
      override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: GlideTarget<Drawable>?,
        isFirstResource: Boolean
      ): Boolean {
        e ?: return true
        callback(e)
        return true
      }
      
      override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: GlideTarget<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
      ): Boolean {
        return true
      }
    })
    return this
  }
  
  @SuppressLint("CheckResult")
  override fun onCompleted(callback: (Drawable) -> Unit): GlideImagesLoader {
    requestBuilder.addListener(object : RequestListener<Drawable> {
      override fun onLoadFailed(
        e: GlideException?,
        model: Any,
        target: GlideTarget<Drawable>,
        isFirstResource: Boolean
      ): Boolean {
        return true
      }
      
      override fun onResourceReady(
        resource: Drawable,
        model: Any,
        target: GlideTarget<Drawable>,
        dataSource: DataSource,
        isFirstResource: Boolean
      ): Boolean {
        callback(resource)
        return true
      }
    })
    return this
  }
  
  @SuppressLint("CheckResult")
  override fun placeholder(placeholderResId: Int): GlideImagesLoader {
    requestBuilder.placeholder(placeholderResId)
    return this
  }
  
  override fun into(imageView: ImageView) {
    requestBuilder.into(imageView)
  }
  
}