package com.arsvechkarev.letta.utils

import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.LettaApplication
import com.arsvechkarev.letta.editing.Container

val Int.dp: Float
  get() {
    check(LettaApplication.density != -1f) { "Density is not initialized yet" }
    return this * LettaApplication.density
  }

val Int.sp: Float
  get() {
    check(LettaApplication.scaledDensity != -1f) { "Density is not initialized yet" }
    return this * LettaApplication.scaledDensity
  }

fun Container.dmFloat(@DimenRes resId: Int): Float {
  return dmInt(resId).f
}

fun Container.dmInt(@DimenRes resId: Int): Int {
  return view.resources.getDimensionPixelSize(resId)
}

fun Fragment.dmFloat(@DimenRes resId: Int): Float {
  return dmInt(resId).f
}

fun Fragment.dmInt(@DimenRes resId: Int): Int {
  return resources.getDimensionPixelSize(resId)
}

fun View.dmFloat(@DimenRes resId: Int): Float {
  return dmInt(resId).f
}

fun View.dmInt(@DimenRes resId: Int): Int {
  return resources.getDimensionPixelSize(resId)
}