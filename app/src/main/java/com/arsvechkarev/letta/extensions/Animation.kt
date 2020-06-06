package com.arsvechkarev.letta.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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