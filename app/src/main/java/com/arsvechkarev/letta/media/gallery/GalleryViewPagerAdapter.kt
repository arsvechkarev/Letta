package com.arsvechkarev.letta.media.gallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.arsvechkarev.letta.media.common.ImagesViewModel

class GalleryViewPagerAdapter(fragment: Fragment, private val imagesViewModel: ImagesViewModel) :
  FragmentStatePagerAdapter(fragment.childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  
  override fun getItem(position: Int): Fragment {
    return SingleImageFragment.create(imagesViewModel.data[position].url)
  }
  
  override fun getCount(): Int {
    return imagesViewModel.data.size
  }
  
}