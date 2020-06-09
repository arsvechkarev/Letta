package com.arsvechkarev.letta.core

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import com.arsvechkarev.letta.extensions.getDimen

fun <V : View> V.withParams(
  @DimenRes width: Int,
  @DimenRes height: Int,
  @DimenRes margins: Int
): V {
  val params = ViewGroup.MarginLayoutParams(
    context.getDimen(width).toInt(),
    context.getDimen(height).toInt()
  )
  val margin = context.getDimen(margins).toInt()
  params.setMargins(margin, margin, margin, margin)
  layoutParams = params
  return this
}