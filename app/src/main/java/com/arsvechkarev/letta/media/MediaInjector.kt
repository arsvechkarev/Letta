package com.arsvechkarev.letta.media

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.imagesloader.GlideImagesLoader
import com.arsvechkarev.imagesloader.ImagesLoader
import com.arsvechkarev.letta.media.common.ImagesViewModel

object MediaInjector {
  
  fun provideImagesViewModel(fragment: Fragment): ImagesViewModel {
    val provider = ViewModelProvider(fragment.requireActivity(),
      provideImagesViewModelFactory(fragment))
    return provider[ImagesViewModel::class.java]
  }
  
  fun getImageLoader(fragment: Fragment): ImagesLoader {
    return GlideImagesLoader(fragment)
  }
  
  private fun provideImagesViewModelFactory(fragment: Fragment): ImagesViewModel.Factory {
    return ImagesViewModel.Factory(fragment.requireActivity().contentResolver)
  }
  
}