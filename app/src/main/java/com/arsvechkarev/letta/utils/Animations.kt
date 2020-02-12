package com.arsvechkarev.letta.utils

import android.view.View
import android.view.ViewPropertyAnimator

fun View.animateAlpha(visible: Boolean, duration: Long) {
  val targetAlpha = if (visible) 1f else 0f
  if (alpha == targetAlpha) return
  visible()
  val anim = this.animate().alpha(targetAlpha).duration(duration)
  if (!visible) {
    anim.withEndAction { gone() }
  }
}

fun ViewPropertyAnimator.duration(duration: Long): ViewPropertyAnimator {
  this.duration = duration
  return this
}