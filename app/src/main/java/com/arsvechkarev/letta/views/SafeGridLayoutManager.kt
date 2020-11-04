package com.arsvechkarev.letta.views

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SafeGridLayoutManager(
  context: Context,
  spanCount: Int,
) : GridLayoutManager(context, spanCount) {
  
  override fun onLayoutChildren(
    recycler: RecyclerView.Recycler?,
    state: RecyclerView.State?
  ) {
    try {
      super.onLayoutChildren(recycler, state)
    } catch (e: Exception) {
    
    }
  }
}