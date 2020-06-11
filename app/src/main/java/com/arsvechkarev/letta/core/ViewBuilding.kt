package com.arsvechkarev.letta.core

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import com.arsvechkarev.letta.extensions.getDimen
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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

@OptIn(ExperimentalContracts::class)
inline fun View.withParams(block: View.(ViewGroup.MarginLayoutParams) -> Unit) {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  block.invoke(this, layoutParams as ViewGroup.MarginLayoutParams)
}

fun View.layoutNormal(left: Int, top: Int, right: Int, bottom: Int) {
  layout(left, top, right, bottom)
}

fun View.layoutWithLeftTop(left: Int, top: Int, params: ViewGroup.LayoutParams) {
  layout(left, top, left + params.width, top + params.height)
}

fun View.layoutAroundMiddlePoint(middleX: Int, middleY: Int) {
  val hw = measuredWidth / 2
  val hh = measuredHeight / 2
  layout(middleX - hw, middleY - hh, middleX + hw, middleY + hh)
}

fun View.layoutAroundMiddlePoint(middleX: Int, middleY: Int, params: ViewGroup.LayoutParams) {
  val hw = params.width / 2
  val hh = params.height / 2
  layout(middleX - hw, middleY - hh, middleX + hw, middleY + hh)
}