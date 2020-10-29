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

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun View.gone() {
  visibility = View.INVISIBLE
}

val View.isNotGone get() = visibility != View.GONE

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

@Suppress("UNCHECKED_CAST")
fun <T : CoordinatorLayout.Behavior<*>> View.behavior(): T {
  return (layoutParams as CoordinatorLayout.LayoutParams).behavior as T
}

inline fun ViewGroup.findChild(predicate: (View) -> Boolean): View {
  for (i in 0 until childCount) {
    val child = getChildAt(i)
    if (predicate(child)) {
      return child
    }
  }
  throw IllegalStateException("No child matching predicate")
}

val View.totalWidth: Int
  get() {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    return params.width + params.rightMargin + params.leftMargin
  }

val View.totalHeight: Int
  get() {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    return params.height + params.topMargin + params.bottomMargin
  }

inline fun <reified T : CoordinatorLayout.Behavior<*>> View.getBehavior(): T {
  return (layoutParams as CoordinatorLayout.LayoutParams).behavior as T
}

inline fun <reified T : CoordinatorLayout.Behavior<*>> View.hasBehavior(): Boolean {
  return (layoutParams as? CoordinatorLayout.LayoutParams)?.behavior as? T != null
}