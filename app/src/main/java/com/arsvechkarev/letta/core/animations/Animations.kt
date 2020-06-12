package com.arsvechkarev.letta.core.animations

import android.view.View
import com.arsvechkarev.letta.core.DURATION_DEFAULT
import com.arsvechkarev.letta.extensions.doOnEnd
import com.arsvechkarev.letta.extensions.invisible

fun View.animateInvisibleAndScale() {
  isClickable = false
  animate().alpha(0f)
      .scaleX(1.2f)
      .withLayer()
      .scaleY(1.2f)
      .setDuration(DURATION_DEFAULT)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .doOnEnd {
        invisible()
        isClickable = true
        scaleX = 1f
        scaleY = 1f
        alpha = 1f
      }
}

fun View.rotate(duration: Long = DURATION_DEFAULT) {
  isClickable = false
  animate()
      .rotation(180f)
      .withLayer()
      .setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .doOnEnd {
        isClickable = true
        rotation = 0f
      }
      .start()
}