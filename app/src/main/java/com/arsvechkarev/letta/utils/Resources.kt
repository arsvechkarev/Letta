package com.arsvechkarev.letta.utils

import android.content.Context
import android.view.ContextMenu
import android.view.View
import androidx.annotation.DimenRes
import com.arsvechkarev.letta.LettaApplication

val Int.dp get() = this * LettaApplication.density

val Float.dp get() = this * LettaApplication.density

val Int.dpInt get() = (this * LettaApplication.density).toInt()

val Float.dpInt get() = (this * LettaApplication.density).toInt()

val Int.sp: Float
  get() {
    check(LettaApplication.scaledDensity != -1f) { "Density is not initialized yet" }
    return this * LettaApplication.scaledDensity
  }

fun View.dmInt(@DimenRes resId: Int): Int {
  return resources.getDimensionPixelSize(resId)
}

fun Context.dimen(@DimenRes resId: Int): Float {
  return resources.getDimension(resId)
}