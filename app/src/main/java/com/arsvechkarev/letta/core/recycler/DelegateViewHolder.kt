package com.arsvechkarev.letta.core.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  @Suppress("PropertyName")
  internal var _item: Any? = null
  
  val item: T
    get() = if (_item == null) {
      throw IllegalArgumentException("Item has not been set yet")
    } else {
      @Suppress("UNCHECKED_CAST")
      _item as T
    }
  
  open fun bind(item: T) = Unit
  
  open fun onViewRecycled() = Unit
}