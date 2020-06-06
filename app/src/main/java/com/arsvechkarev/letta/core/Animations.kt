package com.arsvechkarev.letta.core

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.utils.doOnEnd
import com.arsvechkarev.letta.utils.invisible

fun View.animateInvisibleAndScale() {
  isClickable = false
  animate().alpha(0f)
      .scaleX(1.2f)
      .withLayer()
      .scaleY(1.2f)
      .setDuration(DURATION_DEFAULT)
      .setInterpolator(AccelerateDecelerateInterpolator())
      .doOnEnd {
        invisible()
        isClickable = true
        scaleX = 1f
        scaleY = 1f
        alpha = 1f
      }
}