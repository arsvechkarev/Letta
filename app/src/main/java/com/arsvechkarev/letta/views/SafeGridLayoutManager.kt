package com.arsvechkarev.letta.views

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.core.model.LoadingMoreProjects
import com.arsvechkarev.letta.core.recycler.ListAdapter

class SafeGridLayoutManager(
  adapter: ListAdapter,
  context: Context,
  spanCount: Int,
) : GridLayoutManager(context, spanCount) {
  
  init {
    spanSizeLookup = object : SpanSizeLookup() {
      override fun getSpanSize(position: Int) =
          when (adapter.getItemClassByPosition(position)) {
            LoadingMoreProjects::class -> 3
            else -> 1
          }
    }
  }
  
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