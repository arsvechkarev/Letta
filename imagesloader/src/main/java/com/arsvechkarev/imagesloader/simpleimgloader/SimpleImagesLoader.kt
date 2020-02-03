package com.arsvechkarev.imagesloader.simpleimgloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.arsvechkarev.imagesloader.ImagesLoader

class SimpleImagesLoader : ImagesLoader {
  
  override fun load(url: String): ImagesLoader {
    return this
  }
  
  override fun placeholder(placeholderResId: Int): ImagesLoader {
    return this
  }
  
  override fun onError(callback: (Throwable) -> Unit): ImagesLoader {
    return this
  }
  
  override fun onCompleted(callback: (Drawable) -> Unit): ImagesLoader {
    return this
  }
  
  override fun into(imageView: ImageView) {
  }
}