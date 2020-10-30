package com.arsvechkarev.letta.views.behaviors

import android.view.View
import com.arsvechkarev.letta.extensions.f

class BottomSheetOffsetHelper(
  private val bottomSheet: View,
  private val parentHeight: Int,
  private val layoutTop: Int,
  private var onOpenedPercentageChanged: ((Float) -> Unit)?
) {
  
  fun updateDyOffset(offset: Int): Int {
    val newTop = bottomSheet.top + offset
    val oldTop = bottomSheet.top
    bottomSheet.top = newTop.coerceIn(layoutTop, parentHeight)
    notifyOpenedPercentageChanged()
    return bottomSheet.top - oldTop
  }
  
  fun updateTop(value: Int) {
    bottomSheet.top = value.coerceIn(layoutTop, parentHeight)
    notifyOpenedPercentageChanged()
  }
  
  fun release() {
    onOpenedPercentageChanged = null
  }
  
  private fun notifyOpenedPercentageChanged() {
    val fraction = 1f - (bottomSheet.top - layoutTop).f / (parentHeight - layoutTop)
    onOpenedPercentageChanged?.invoke(fraction)
  }
}