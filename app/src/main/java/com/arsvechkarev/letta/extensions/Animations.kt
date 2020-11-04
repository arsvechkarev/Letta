package com.arsvechkarev.letta.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import com.arsvechkarev.letta.core.DURATION_DEFAULT
import com.arsvechkarev.letta.core.DURATION_SHORT

val LinearInterpolator = LinearInterpolator()
val AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
val EndOvershootInterpolator = OvershootInterpolator()

fun View.animateInvisibleAndScale() {
  animate().alpha(0f)
      .scaleX(1.2f)
      .scaleY(1.2f)
      .withLayer()
      .setDuration(DURATION_DEFAULT)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        invisible()
        scaleX = 1f
        scaleY = 1f
        alpha = 1f
      }
}

fun View.animateSquash(andThen: () -> Unit = {}) {
  scaleY = 1f
  animate().scaleY(0f)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .setDuration(DURATION_SHORT)
      .withLayer()
      .withEndAction {
        andThen()
        gone()
      }
      .start()
}

fun View.animateLoosen(andThen: () -> Unit = {}) {
  scaleY = 0f
  visible()
  animate().scaleY(1f)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .setDuration(DURATION_SHORT)
      .withLayer()
      .withEndAction(andThen)
      .start()
}

fun View.rotate(duration: Long = DURATION_DEFAULT) {
  animate()
      .rotation(180f)
      .withLayer()
      .setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
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