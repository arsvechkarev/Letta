package com.arsvechkarev.letta.views.behaviors

import android.view.View
import com.arsvechkarev.letta.utils.lerpColor

class ShadowSlideListener(private val shadowView: View) : BottomSheetBehavior.SlideListener {
  
  override fun onSlide(slidePercent: Float) {
    val color = lerpColor(0, 0x66000000, slidePercent)
    shadowView.setBackgroundColor(color)
  }
}