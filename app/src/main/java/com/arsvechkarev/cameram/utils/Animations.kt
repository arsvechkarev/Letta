package com.arsvechkarev.cameram.utils

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isGone
import androidx.core.view.isVisible

fun View.animateAlpha(visible: Boolean, duration: Long) {
  val targetAlpha = if (visible) 1f else 0f
  if (alpha == targetAlpha) return
  isVisible = true
  val anim = this.animate().alpha(targetAlpha).duration(duration)
  if (!visible) {
    anim.withEndAction { isGone = true }
  }
}

fun ViewPropertyAnimator.duration(duration: Long): ViewPropertyAnimator {
  this.duration = duration
  return this
}