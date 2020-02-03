package com.arsvechkarev.letta.media

import androidx.fragment.app.Fragment
import com.arsvechkarev.imagesloader.GlideImagesLoader
import com.arsvechkarev.imagesloader.ImagesLoader

object MediaInjector {
  
  fun getImageLoader(fragment: Fragment): ImagesLoader {
    return GlideImagesLoader(fragment)
  }
  
}