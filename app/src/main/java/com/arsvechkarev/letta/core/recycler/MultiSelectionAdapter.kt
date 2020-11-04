package com.arsvechkarev.letta.core.recycler

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

abstract class MultiSelectionAdapter<T>(
  onReadyToLoadFurtherData: (() -> Unit)? = null
) : ListAdapter(onReadyToLoadFurtherData = onReadyToLoadFurtherData) {
  
  private var isInSelectionMode = false
  
  protected val selectedItems = ArrayList<T>()
  
  protected val isInSelectionModeLambda = { isInSelectionMode }
  
  fun switchToSelectionMode() {
    isInSelectionMode = true
    onSwitchingToSelectionMode(recyclerView!!.layoutManager as LinearLayoutManager)
  }
  
  fun switchBackFromSelectionMode() {
    isInSelectionMode = false
    selectedItems.clear()
    onSwitchingBackFromSelectionMode(recyclerView!!.layoutManager as LinearLayoutManager)
  }
  
  abstract fun onSwitchingToSelectionMode(layoutManager: LinearLayoutManager)
  
  abstract fun onSwitchingBackFromSelectionMode(layoutManager: LinearLayoutManager)
  
  abstract class MultiSelectionViewHolder<T>(
    itemView: View,
    private val selectedPositions: ArrayList<T>,
    private val isInSelectionMode: () -> Boolean,
    onItemClick: (item: T) -> Unit,
  ) : DelegateViewHolder<T>(itemView) {
    
    abstract fun viewForClickListener(itemView: View): View
    
    abstract fun setSelectedWithoutAnimation(itemView: View)
    
    abstract fun setDisabledWithoutAnimation(itemView: View)
    
    abstract fun setSelected(itemView: View)
    
    abstract fun setDisabled(itemView: View)
    
    abstract fun goToSelectionModeWithoutAnimation(itemView: View)
    
    abstract fun switchFromSelectionModeWithoutAnimation(itemView: View)
    
    abstract fun bindItem(item: T)
    
    init {
      @Suppress("LeakingThis")
      viewForClickListener(itemView).setOnClickListener {
        if (!isInSelectionMode()) {
          onItemClick(item)
        } else {
          if (selectedPositions.contains(item)) {
            setDisabled(itemView)
            selectedPositions.remove(item)
          } else {
            setSelected(itemView)
            selectedPositions.add(item)
          }
        }
      }
    }
    
    override fun bind(item: T) {
      bindItem(item)
      if (isInSelectionMode()) {
        goToSelectionModeWithoutAnimation(itemView)
        if (selectedPositions.contains(item)) {
          setSelectedWithoutAnimation(itemView)
        } else {
          setDisabledWithoutAnimation(itemView)
        }
      } else {
        switchFromSelectionModeWithoutAnimation(itemView)
        setDisabledWithoutAnimation(itemView)
      }
    }
  }
}