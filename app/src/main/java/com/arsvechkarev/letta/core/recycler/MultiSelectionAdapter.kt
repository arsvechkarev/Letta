package com.arsvechkarev.letta.core.recycler

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.letta.core.recycler.MultiSelectionAdapter.MultiSelectionViewHolder

abstract class MultiSelectionAdapter<D, VH : MultiSelectionViewHolder<D>>(
  data: List<D> = ArrayList()
) : BaseAdapter<D, MultiSelectionViewHolder<D>>(data) {
  
  protected val selectedItemPositions = ArrayList<Int>()
  
  private var isInSelectionMode = false
  
  protected val isInSelectionModeLambda = { isInSelectionMode }
  
  fun switchToSelectionMode() {
    isInSelectionMode = true
    onSwitchingToSelectionMode(recyclerView!!.layoutManager as LinearLayoutManager)
  }
  
  fun switchBackFromSelectionMode() {
    isInSelectionMode = false
    selectedItemPositions.clear()
    onSwitchingBackFromSelectionMode(recyclerView!!.layoutManager as LinearLayoutManager)
  }
  
  abstract fun onSwitchingToSelectionMode(layoutManager: LinearLayoutManager)
  
  abstract fun onSwitchingBackFromSelectionMode(layoutManager: LinearLayoutManager)
  
  abstract class MultiSelectionViewHolder<D>(
    itemView: View,
    private val selectedPositions: ArrayList<Int>,
    private val isInSelectionMode: () -> Boolean,
    onItemClick: (position: Int) -> Unit,
  ) : BaseAdapter.BaseViewHolder<D>(itemView) {
    
    abstract fun viewForClickListener(itemView: View): View
    
    abstract fun setSelectedWithoutAnimation(itemView: View)
    
    abstract fun setDisabledWithoutAnimation(itemView: View)
    
    abstract fun setSelected(itemView: View)
    
    abstract fun setDisabled(itemView: View)
    
    abstract fun goToSelectionModeWithoutAnimation(itemView: View)
    
    abstract fun switchFromSelectionModeWithoutAnimation(itemView: View)
    
    abstract fun bindItem(item: D)
    
    init {
      @Suppress("LeakingThis")
      viewForClickListener(itemView).setOnClickListener {
        if (!isInSelectionMode()) {
          onItemClick(adapterPosition)
        } else {
          if (selectedPositions.contains(adapterPosition)) {
            setDisabled(itemView)
            selectedPositions.remove(adapterPosition)
          } else {
            setSelected(itemView)
            selectedPositions.add(adapterPosition)
          }
        }
      }
    }
    
    override fun bind(item: D) {
      bindItem(item)
      if (isInSelectionMode()) {
        goToSelectionModeWithoutAnimation(itemView)
        if (selectedPositions.contains(adapterPosition)) {
          setSelectedWithoutAnimation(itemView)
        } else {
          setDisabledWithoutAnimation(itemView)
        }
      } else {
        switchFromSelectionModeWithoutAnimation(itemView)
      }
    }
  }
}