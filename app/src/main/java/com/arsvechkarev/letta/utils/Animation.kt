package com.arsvechkarev.letta.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.ViewPropertyAnimator

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
