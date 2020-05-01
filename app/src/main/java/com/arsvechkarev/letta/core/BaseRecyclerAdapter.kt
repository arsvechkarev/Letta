package com.arsvechkarev.letta.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T, VH : BaseRecyclerAdapter.BaseViewHolder<T>>(
  private val data: List<T>
) : RecyclerView.Adapter<VH>() {
  
  override fun getItemCount() = data.size
  
  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.bind(data[position])
  }
  
  abstract class BaseViewHolder<D>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    abstract fun bind(item: D)
  }
  
}