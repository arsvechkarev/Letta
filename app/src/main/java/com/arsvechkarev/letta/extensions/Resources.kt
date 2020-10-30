package com.arsvechkarev.letta.extensions

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import com.arsvechkarev.letta.LettaApplication
import java.util.Locale


val Int.dp get() = this * LettaApplication.density

val Float.dp get() = this * LettaApplication.density

val Int.dpInt get() = (this * LettaApplication.density).toInt()

val isLayoutLeftToRight: Boolean
  get() = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_LTR

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

fun Context.getStatusBarHeight(): Int {
  var result = 0
  val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
  if (resourceId > 0) {
    result = resources.getDimensionPixelSize(resourceId)
  }
  return result
}
