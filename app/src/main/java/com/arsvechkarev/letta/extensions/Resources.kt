package com.arsvechkarev.letta.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.res.ResourcesCompat
import com.arsvechkarev.letta.LettaApplication

val Int.dp get() = this * LettaApplication.density

val Float.dp get() = this * LettaApplication.density

val Int.dpInt get() = (this * LettaApplication.density).toInt()

fun Context.getDimen(@DimenRes resId: Int): Float {
  return resources.getDimension(resId)
}

fun Context.retrieveColor(@ColorRes resId: Int): Int {
  return ResourcesCompat.getColor(resources, resId, theme)
}