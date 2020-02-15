package com.arsvechkarev.letta.editing

import android.view.View
import com.arsvechkarev.letta.constants.DURATION_DEFAULT
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.gone

fun View.animateToolMoveToTop(desiredSize: Int, desiredTop: Float) {
  if (this.top + translationY == desiredTop || width == desiredSize) return
  val scale = desiredSize / width.f
  val translation = top - desiredTop
  this.animate()
      .scaleX(scale)
      .scaleY(scale)
      .setDuration(DURATION_DEFAULT)
      .translationYBy(-translation)
      .start()
}

fun View.animateToolHiding() {
  if (this.alpha == 0f) return
  val translation = width.f / 4
  animate()
      .alpha(0f)
      .setDuration(DURATION_DEFAULT)
      .translationXBy(translation)
      .withEndAction { gone() }
      .start()
}
