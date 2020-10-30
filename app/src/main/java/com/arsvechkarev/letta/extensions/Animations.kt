package com.arsvechkarev.letta.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import com.arsvechkarev.letta.core.DURATION_DEFAULT

val LinearInterpolator = LinearInterpolator()
val AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
val EndOvershootInterpolator = OvershootInterpolator()

fun View.animateInvisibleAndScale() {
  isClickable = false
  animate().alpha(0f)
      .scaleX(1.2f)
      .withLayer()
      .scaleY(1.2f)
      .setDuration(DURATION_DEFAULT)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        invisible()
        isClickable = true
        scaleX = 1f
        scaleY = 1f
        alpha = 1f
      }
}

fun View.rotate(duration: Long = DURATION_DEFAULT) {
  isClickable = false
  animate()
      .rotation(180f)
      .withLayer()
      .setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        isClickable = true
        rotation = 0f
      }
      .start()
}

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}

fun Animator.doOnEnd(block: () -> Unit) {
  addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
      removeListener(this)
    }
  })
}