package com.arsvechkarev.letta.media.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.CommonInjector
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.media.MediaInjector
import com.arsvechkarev.letta.media.common.ImagesViewModel
import kotlinx.android.synthetic.main.fragment_single_image.imageSingleItem

class SingleImageFragment : Fragment(R.layout.fragment_single_image) {
  
  private val imageLoader = CommonInjector.getImageLoader(this)
  private lateinit var imageViewModel: ImagesViewModel
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    imageViewModel = MediaInjector.provideImagesViewModel(this)
    val url = arguments!!.getString(IMAGE_URL)!!
    imageSingleItem.transitionName = url
    
    imageLoader.load(url)
      .onError { parentFragment!!.startPostponedEnterTransition() }
      .onSuccess { parentFragment!!.startPostponedEnterTransition() }
      .into(imageSingleItem)
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