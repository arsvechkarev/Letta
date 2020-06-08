package com.arsvechkarev.letta.core.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<D, VH : BaseAdapter.BaseViewHolder<D>>(
  data: List<D> = emptyList()
) : RecyclerView.Adapter<VH>() {
  
  var recyclerView: RecyclerView? = null
    private set
  
  var data: List<D> = data
    protected set
  
  fun submitList(data: List<D>) {
    this.data = data
    notifyDataSetChanged()
  }
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = recyclerView
  }
  
  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.bind(data[position])
  }
  
  override fun getItemCount() = data.size
  
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = null
  }
  
  abstract class BaseViewHolder<D>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    abstract fun bind(item: D)
  }
}