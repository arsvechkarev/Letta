package com.arsvechkarev.letta.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<D, VH : BaseRecyclerAdapter.BaseViewHolder<D>>(
  val list: List<D>
) : RecyclerView.Adapter<VH>() {
  
  override fun getItemCount() = list.size
  
  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.bind(list[position], position)
  }
  
  abstract class BaseViewHolder<D>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    abstract fun bind(item: D, position: Int)
  }
  
}