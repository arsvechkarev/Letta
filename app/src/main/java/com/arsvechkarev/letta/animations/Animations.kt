package com.arsvechkarev.letta.animations

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.arsvechkarev.letta.utils.doOnEnd
import com.arsvechkarev.letta.utils.gone
import com.arsvechkarev.letta.utils.visible

const val DURATION_SMALL = 170L
const val DURATION_DEFAULT = 250L

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

fun View.animateColor(startColor: Int, endColor: Int, andThen: () -> Unit = {}) {
  ObjectAnimator.ofObject(this,
    "backgroundColor", ArgbEvaluator(), startColor, endColor).apply {
    duration = DURATION_DEFAULT
    interpolator = FastOutSlowInInterpolator()
    if (andThen != {}) {
      doOnEnd(andThen)
    }
    start()
  }
}