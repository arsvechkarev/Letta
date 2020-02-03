package com.arsvechkarev.letta.media.gallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter

class GalleryViewPager(fragment: Fragment, val itemCount: Int) :
  FragmentStatePagerAdapter(fragment.childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  
  // TODO (2/3/2020): Add urls provider
  override fun getItem(position: Int): Fragment {
    return SingleImageFragment.create("")
  }
  
  override fun getCount(): Int {
    return 0
  }
  
}