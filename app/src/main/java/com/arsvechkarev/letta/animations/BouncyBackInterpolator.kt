package com.arsvechkarev.letta.animations

import android.animation.ValueAnimator
import android.view.animation.Interpolator

class BouncyBackInterpolator(
  private val value: Float,
  private val coefficient: Float,
  private val inTheMiddle: () -> Unit
) : Interpolator {
  
  private var middleBlockExecuted = false
  
  override fun getInterpolation(input: Float): Float {
    return if (input < value / coefficient) {
      input
    } else {
      if (!middleBlockExecuted) inTheMiddle()
      middleBlockExecuted = true
      1 - input
    }
  }
  
}

fun ValueAnimator.addBouncyBackEffect(
  value: Float,
  coefficient: Float = 2f,
  inTheMiddle: () -> Unit = {}
) {
  setFloatValues(value, value * coefficient)
  interpolator = BouncyBackInterpolator(value, coefficient, inTheMiddle)
}