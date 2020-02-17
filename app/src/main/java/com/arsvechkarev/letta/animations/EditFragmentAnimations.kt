package com.arsvechkarev.letta.animations

import android.view.View
import com.arsvechkarev.letta.constants.DURATION_DEFAULT
import com.arsvechkarev.letta.utils.f

fun View.animateToolMoveToTop(desiredTop: Float) {
  val translation = top - desiredTop
  tag = translation
  isClickable = false
  this.animate()
      .scaleX(1.15f)
      .scaleY(1.15f)
      .setDuration(DURATION_DEFAULT)
      .yBy(-translation)
      .withEndAction { isClickable = true }
      .start()
}

fun View.animateToolMoveBack() {
  val translation = tag as Float
  isClickable = false
  this.animate()
      .configure()
      .scaleX(1f)
      .scaleY(1f)
      .yBy(translation)
      .withEndAction { isClickable = true }
      .start()
}

fun View.animateToolHiding() {
  val translation = width.f / 4
  isClickable = false
  animate()
      .configure()
      .alpha(0f)
      .xBy(translation)
      .start()
}

fun View.animateToolAppearing() {
  val translation = width.f / 4
  animate()
      .configure()
      .alpha(1f)
      .xBy(-translation)
      .withEndAction { isClickable = true }
      .start()
}
