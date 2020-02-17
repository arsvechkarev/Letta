package com.arsvechkarev.letta.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Property
import android.view.View
import com.arsvechkarev.letta.animations.configure

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
