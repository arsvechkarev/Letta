package com.arsvechkarev.letta.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun View.gone() {
  visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.constraints(block: ConstraintLayout.LayoutParams.() -> Unit) {
  layoutParams = (this.layoutParams as ConstraintLayout.LayoutParams).apply(block)
}
fun View.layoutParams(block: ViewGroup.MarginLayoutParams.() -> Unit) {
  layoutParams = (this.layoutParams as ViewGroup.MarginLayoutParams).apply(block)
}