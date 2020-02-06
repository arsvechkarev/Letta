package com.arsvechkarev.letta.media.gallery

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.media.MediaInjector
import com.arsvechkarev.letta.media.common.ImagesViewModel
import kotlinx.android.synthetic.main.fragment_gallery_pager.photosViewPager

class GalleryPagerFragment : Fragment(R.layout.fragment_gallery_pager) {
  
  private lateinit var imagesViewModel: ImagesViewModel
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    imagesViewModel = MediaInjector.provideImagesViewModel(this)
    photosViewPager.adapter = GalleryViewPagerAdapter(this, imagesViewModel)
    photosViewPager.currentItem = imagesViewModel.currentPosition
    photosViewPager.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
      override fun onPageSelected(position: Int) {
        imagesViewModel.currentPosition = position
      }
    })
    if (savedInstanceState == null) {
      postponeEnterTransition()
    }
    prepareSharedElementTransition()
  }
  
  /**
   * Prepares the shared element transition from and back to the grid fragment.
   */
  private fun prepareSharedElementTransition() {
    val transition = TransitionInflater.from(context)
      .inflateTransition(R.transition.image_shared_element_transition)
    sharedElementEnterTransition = transition
    // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
    setEnterSharedElementCallback(
      object : SharedElementCallback() {
        override fun onMapSharedElements(
          names: List<String?>,
          sharedElements: MutableMap<String?, View?>
        ) { // Locate the image view at the primary fragment (the ImageFragment that is currently
          // visible). To locate the fragment, call instantiateItem with the selection position.
          // At this stage, the method will simply return the fragment at the position and will
          // not create a new one.
          val currentFragment = photosViewPager.adapter!!.instantiateItem(
            photosViewPager, imagesViewModel.currentPosition
          ) as Fragment
          val view = currentFragment.view ?: return
          // Map the first shared element name to the child ImageView.
          sharedElements[names[0]] = view.findViewById(R.id.image)
        }
      })
  }
  
}