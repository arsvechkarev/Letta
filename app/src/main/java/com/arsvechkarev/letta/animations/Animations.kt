package com.arsvechkarev.letta.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Property
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.utils.gone
import com.arsvechkarev.letta.utils.visible

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


fun ValueAnimator.doOnUpdate(block: ValueAnimator.() -> Unit): ValueAnimator {
  addUpdateListener(block)
  return this
}

fun ValueAnimator.doOnEnd(block: () -> Unit): ValueAnimator {
  this.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
    }
  })
  return this
}

fun ObjectAnimator.doOnEnd(block: () -> Unit): ObjectAnimator {
  this.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
    }
  })
  return this
}

fun View.animate(property: Property<View, Float>, vararg values: Float, onEnd: () -> Unit = {}) {
  this.isClickable = false
  ObjectAnimator.ofFloat(this, property, *values).configure()
      .doOnEnd {
        isClickable = true
        onEnd()
      }
      .start()
}
