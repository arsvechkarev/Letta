package com.arsvechkarev.letta.extensions

import android.animation.ValueAnimator
import android.view.animation.OvershootInterpolator

class MiddleBlockInterpolator(
  private val middlePercent: Float,
  private val inTheMiddle: () -> Unit
) : OvershootInterpolator() {
  
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
  setFloatValues(1f, 1.20f, 1f)
  interpolator = MiddleBlockInterpolator(0.3f, inTheMiddle)
}