package com.arsvechkarev.letta.animations

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.constants.DURATION_DEFAULT
import com.arsvechkarev.letta.utils.gone
import com.arsvechkarev.letta.utils.visible

fun ViewPropertyAnimator.configure(): ViewPropertyAnimator {
  duration = DURATION_DEFAULT
  interpolator = AccelerateDecelerateInterpolator()
  return this
}

fun ObjectAnimator.configure(): ObjectAnimator {
  duration = DURATION_DEFAULT
  interpolator = AccelerateDecelerateInterpolator()
  return this
}

fun View.fadeIn() {
  alpha = 0f
  visible()
  animate()
      .configure()
      .alpha(1f)
      .withEndAction { isClickable = true }
      .start()
}

fun View.fadeOut(andThen: () -> Unit = {}) {
  isClickable = false
  animate()
      .configure()
      .alpha(0f)
      .withEndAction {
        gone()
        andThen()
      }
      .start()
}
