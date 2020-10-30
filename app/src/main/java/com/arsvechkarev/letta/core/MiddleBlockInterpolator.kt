package com.arsvechkarev.letta.core

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator

class MiddleBlockInterpolator(
  private val middlePercent: Float,
  private val inTheMiddle: () -> Unit
) : AccelerateDecelerateInterpolator() {
  
  private var middleBlockExecuted = false
  
  override fun getInterpolation(input: Float): Float {
    if (!middleBlockExecuted && input > middlePercent) {
      inTheMiddle()
      middleBlockExecuted = true
    }
    return super.getInterpolation(input)
  }
}

fun ValueAnimator.addBouncyBackEffect(
  inTheMiddle: () -> Unit = {}
) {
  setFloatValues(1f, 1.16f, 0.96f, 1.02f, 1f)
  interpolator = MiddleBlockInterpolator(0.33f, inTheMiddle)
}