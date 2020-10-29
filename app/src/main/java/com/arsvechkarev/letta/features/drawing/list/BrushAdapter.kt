package com.arsvechkarev.letta.features.drawing.list

import android.view.View
import android.view.ViewGroup
import com.arsvechkarev.letta.R.dimen.brush_example_margin
import com.arsvechkarev.letta.R.dimen.brush_example_size
import com.arsvechkarev.letta.core.recycler.SingleSelectionAdapter
import com.arsvechkarev.letta.core.withParams
import com.arsvechkarev.letta.features.drawing.list.BrushAdapter.BrushViewHolder
import com.arsvechkarev.letta.opengldrawing.brushes.Brush
import com.arsvechkarev.letta.views.BrushExampleView

class BrushAdapter(
  items: List<Brush>,
  private val onBrushSelected: (Brush) -> Unit
) : SingleSelectionAdapter<Brush, BrushViewHolder>(items) {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrushViewHolder {
    return BrushViewHolder(
      BrushExampleView(parent.context).withParams(
        width = brush_example_size,
        height = brush_example_size,
        margin = brush_example_margin
      )
    )
  }
  
  inner class BrushViewHolder(itemView: View) : BaseViewHolder<Brush>(itemView) {
    
    init {
      itemView.setOnClickListener {
        if (selectedPosition == adapterPosition) {
          // Already selected
          return@setOnClickListener
        }
        val previousViewHolder = getSelectedViewHolder()
        if (previousViewHolder == null) {
          // Holder is outside of range
          val previousPosition = selectedPosition
          notifyItemChanged(previousPosition)
        } else {
          (previousViewHolder.itemView as BrushExampleView).isSelected = false
        }
        selectedPosition = adapterPosition
        itemView.isSelected = true
        onBrushSelected(data[adapterPosition])
      }
    }
    
    override fun bind(item: Brush) {
      val brushExampleView = itemView as BrushExampleView
      brushExampleView.updateBitmap(item)
      brushExampleView.isSelected = selectedPosition == adapterPosition
    }
  }
}