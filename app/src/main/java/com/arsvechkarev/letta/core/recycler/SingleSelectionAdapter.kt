package com.arsvechkarev.letta.core.recycler

import com.arsvechkarev.letta.core.recycler.BaseAdapter.BaseViewHolder

abstract class SingleSelectionAdapter<D, VH : BaseViewHolder<D>>(
  data: List<D> = ArrayList()
) : BaseAdapter<D, VH>(data) {
  
  protected var selectedPosition: Int = 0
  
  @Suppress("UNCHECKED_CAST")
  fun getSelectedViewHolder(): VH? {
    return recyclerView!!.findViewHolderForAdapterPosition(selectedPosition) as? VH
  }
}