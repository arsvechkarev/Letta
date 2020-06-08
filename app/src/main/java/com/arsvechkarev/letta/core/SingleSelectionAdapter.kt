package com.arsvechkarev.letta.core

import androidx.recyclerview.widget.RecyclerView

abstract class SingleSelectionAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
  
  private var recyclerView: RecyclerView? = null
  protected var selectedPosition: Int = 0
  
  @Suppress("UNCHECKED_CAST")
  fun getSelectedView(): VH? {
    return recyclerView!!.findViewHolderForAdapterPosition(selectedPosition) as? VH
  }
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = recyclerView
  }
  
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = null
  }
}