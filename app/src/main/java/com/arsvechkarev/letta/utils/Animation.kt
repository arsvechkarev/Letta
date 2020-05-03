package com.arsvechkarev.letta.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.Animatable
import android.view.ViewPropertyAnimator

fun Animatable.stopIfRunning() {
  if (isRunning) stop()
}

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}

fun Animator.doOnEnd(block: () -> Unit) {
  addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
    }
  })
}

fun ViewPropertyAnimator.doOnEnd(block: () -> Unit): ViewPropertyAnimator {
  setListener(object : AnimatorListenerAdapter() {
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
