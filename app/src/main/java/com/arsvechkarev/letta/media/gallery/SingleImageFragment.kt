package com.arsvechkarev.letta.media.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.media.MediaInjector

class SingleImageFragment : Fragment(R.layout.fragment_single_image) {
  
  private val imageLoader = MediaInjector.getImageLoader(this)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  
  }
  
  companion object {
    
    private const val IMAGE_URL = "IMAGE_URL"
    
    fun create(imageUrl: String) = SingleImageFragment().apply {
      arguments = Bundle().apply {
        putString(IMAGE_URL, imageUrl)
      }
    }
  }
  
}