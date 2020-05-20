package com.arsvechkarev.letta.views.behaviors

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

fun scrollHeader(parent: CoordinatorLayout, header: View, dy: Int): Boolean {
  if (dy > 0 && parent.top <= header.top) // Already at the top
    return false
  val offset: Int
  if (dy > 0) {
    offset = dy.coerceAtMost(parent.top - header.top)
  } else {
    offset = dy
  }
  ViewCompat.offsetTopAndBottom(header, offset)
  return true
}