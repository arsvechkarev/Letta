package com.arsvechkarev.letta.views.behaviors

import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.extensions.ifNotNull

fun RecyclerView.allowRecyclerScrolling(): Boolean {
  adapter.ifNotNull { adapter ->
    var pos = -1
    for (i in 0 until childCount) {
      val view = getChildAt(i)
      if (view.bottom > height) {
        pos = i
      }
    }
    if (pos == -1) {
      return false
    }
    return pos < adapter.itemCount
  }
  return false
}
