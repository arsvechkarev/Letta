package com.arsvechkarev.imagesloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImagesLoader {
  
  fun load(url: String): ImagesLoader
  
  fun placeholder(@DrawableRes placeholderResId: Int): ImagesLoader
  
  fun onError(callback: (Throwable) -> Unit): ImagesLoader
  
  fun onSuccess(callback: (Drawable) -> Unit): ImagesLoader
  
  fun into(imageView: ImageView)
  
}