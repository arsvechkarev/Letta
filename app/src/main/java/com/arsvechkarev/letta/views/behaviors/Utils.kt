package com.arsvechkarev.letta.views.behaviors

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

fun scrollHeader(parent: CoordinatorLayout, header: View, dy: Int): Int {
  if (dy > 0 && parent.top <= header.top) // Already at the top
    return 0
  val offset: Int
  if (dy > 0) {
    offset = dy.coerceAtMost(parent.top - header.top)
  } else {
    offset = dy
  }
  ViewCompat.offsetTopAndBottom(header, offset)
  return offset
}