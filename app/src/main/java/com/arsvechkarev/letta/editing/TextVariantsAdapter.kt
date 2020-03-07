package com.arsvechkarev.letta.editing

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.BaseRecyclerAdapter
import com.arsvechkarev.letta.graphics.TextVariant
import com.arsvechkarev.letta.graphics.textVariantsList
import com.arsvechkarev.letta.utils.extenstions.inflate
import kotlinx.android.synthetic.main.item_example_text.view.textViewVariant

class TextVariantsAdapter(private val onClick: (TextVariant) -> Unit) :
  BaseRecyclerAdapter<TextVariant, TextVariantsAdapter.ExampleTextVH>(textVariantsList) {
  
  private var selectedPosition = 0
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleTextVH {
    return ExampleTextVH(parent.inflate(R.layout.item_example_text))
  }
  
  inner class ExampleTextVH(itemView: View) : BaseViewHolder<TextVariant>(itemView) {
    
    private val textVariant = itemView.textViewVariant
    
    init {
      itemView.setOnClickListener {
        setSelectedView()
        if (selectedPosition != adapterPosition) {
          notifyItemChanged(selectedPosition)
          selectedPosition = adapterPosition
        }
      }
    }
    
    override fun bind(item: TextVariant) {
      if (selectedPosition == adapterPosition) {
        setSelectedView()
      } else {
        setDefaultView()
      }
      textVariant.draw(item)
//      textVariant.setText(item.text)
//      textVariant.setTextSize(25f)
//      textVariant.setTextColor(item.paint.color)
    }
    
    private fun setSelectedView() {
      itemView.setBackgroundColor(Color.GRAY)
    }
    
    private fun setDefaultView() {
      itemView.setBackgroundColor(Color.BLACK)
    }
  }
}