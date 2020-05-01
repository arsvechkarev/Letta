package com.arsvechkarev.letta.utils

import android.view.View
import androidx.annotation.DimenRes
import com.arsvechkarev.letta.LettaApplication

val Int.dp: Float
  get() {
    check(LettaApplication.density != -1f) { "Density is not initialized yet" }
    return this * LettaApplication.density
  }

val Float.dp: Float
  get() {
    check(LettaApplication.density != -1f) { "Density is not initialized yet" }
    return this * LettaApplication.density
  }

val Int.sp: Float
  get() {
    check(LettaApplication.scaledDensity != -1f) { "Density is not initialized yet" }
    return this * LettaApplication.scaledDensity
  }

fun View.dmInt(@DimenRes resId: Int): Int {
  return resources.getDimensionPixelSize(resId)
}