package com.arsvechkarev.letta.utils

import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.LettaApplication
import com.arsvechkarev.letta.core.Container

val Int.dp: Float
  get() = this * LettaApplication.density

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