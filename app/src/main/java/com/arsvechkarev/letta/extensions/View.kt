package com.arsvechkarev.letta.extensions

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout

operator fun View.contains(event: MotionEvent): Boolean {
  val x = event.x
  val y = event.y
  return x >= 0 && y >= 0 && x <= width && y <= height
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

@Suppress("UNCHECKED_CAST")
fun <T : CoordinatorLayout.Behavior<*>> View.behavior(): T {
  return (layoutParams as CoordinatorLayout.LayoutParams).behavior as T
}