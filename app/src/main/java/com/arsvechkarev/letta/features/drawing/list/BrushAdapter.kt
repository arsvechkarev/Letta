package com.arsvechkarev.letta.features.drawing.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.core.recycler.SingleSelectionAdapter
import com.arsvechkarev.letta.features.drawing.list.BrushAdapter.BrushViewHolder
import com.arsvechkarev.letta.opengldrawing.brushes.Brush
import com.arsvechkarev.letta.views.BrushExampleView

class BrushAdapter(
  items: List<Brush>,
  private val onBrushSelected: (Brush) -> Unit
) : SingleSelectionAdapter<Brush, BrushViewHolder>(items) {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrushViewHolder {
    val brushExampleView = BrushExampleView(parent.context)
    val params = RecyclerView.LayoutParams(100, 100)
    val margin = 50
    params.setMargins(margin, margin, margin, margin)
    brushExampleView.layoutParams = params
    return BrushViewHolder(brushExampleView)
  }
  
  inner class BrushViewHolder(itemView: View) : BaseViewHolder<Brush>(itemView) {
    
    init {
      itemView.setOnClickListener {
        onBrushSelected(data[adapterPosition])
      }
    }
    
    override fun bind(item: Brush) {
      (itemView as BrushExampleView).updateBitmap(item)
    }
  }
}