package com.arsvechkarev.letta.animations

import android.animation.ObjectAnimator
import android.view.ViewPropertyAnimator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.arsvechkarev.letta.constants.DURATION_DEFAULT


fun ViewPropertyAnimator.configure(): ViewPropertyAnimator {
  duration = DURATION_DEFAULT
  interpolator = FastOutSlowInInterpolator()
  return this
}

fun ObjectAnimator.configure(): ObjectAnimator {
  duration = DURATION_DEFAULT
  interpolator = FastOutSlowInInterpolator()
  return this
}
