package com.arsvechkarev.letta.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.res.ResourcesCompat
import com.arsvechkarev.letta.LettaApplication

val Int.dp get() = this * LettaApplication.density

val Float.dp get() = this * LettaApplication.density

val Int.dpInt get() = (this * LettaApplication.density).toInt()

@ColorInt
fun Context.getAttrColor(@AttrRes resId: Int): Int {
  val typedValue = TypedValue()
  val resolved = theme.resolveAttribute(resId, typedValue, true)
  require(resolved) { "Attribute cannot be resolved" }
  return typedValue.data
}

fun Context.getDimen(@DimenRes resId: Int): Float {
  return resources.getDimension(resId)
}

fun Context.retrieveColor(@ColorRes resId: Int): Int {
  return ResourcesCompat.getColor(resources, resId, theme)
}