package com.arsvechkarev.letta.extensions

import android.animation.ValueAnimator
import android.view.animation.Interpolator

class BouncyBackInterpolator(
  private val coefficient: Float,
  private val inTheMiddle: () -> Unit
) : Interpolator {
  
  private var middleBlockExecuted = false
  
  override fun getInterpolation(input: Float): Float {
    if (!middleBlockExecuted && input > 0.5f) {
      inTheMiddle()
      middleBlockExecuted = true
    }
    return coefficient * (-4 * input * input + 4 * input)
  }
}

fun ValueAnimator.addBouncyBackEffect(
  value: Float,
  coefficient: Float = 2f,
  inTheMiddle: () -> Unit = {}
) {
  setFloatValues(value, value * 2)
  interpolator = BouncyBackInterpolator(
    coefficient, inTheMiddle)
}