package com.arsvechkarev.letta.media

import androidx.fragment.app.Fragment

object MediaDiInjector {
  
  fun getImageLoader(fragment: Fragment): ImagesLoader {
    return GlideImagesLoader(fragment)
  }
  
}