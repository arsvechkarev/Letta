package com.arsvechkarev.letta.photoslist

import androidx.fragment.app.Fragment

object Injector {
  
  fun getImageLoader(fragment: Fragment): ImagesLoader {
    return GlideImagesLoader(fragment)
  }
  
}