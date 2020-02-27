package com.arsvechkarev.letta.editing

import android.graphics.Color
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.BaseRecyclerAdapter
import com.arsvechkarev.letta.graphics.TextVariant
import com.arsvechkarev.letta.graphics.textVariantsList
import com.arsvechkarev.letta.utils.constraints
import com.arsvechkarev.letta.utils.inflate
import com.arsvechkarev.letta.views.GradientPalette
import com.arsvechkarev.letta.views.ListenableConstraintLayout
import kotlinx.android.synthetic.main.item_example_text.view.textExample

class TextContainer(
  view: ListenableConstraintLayout,
  private val topControlView: View,
  private val onTextEntered: (CharSequence, TextVariant) -> Unit
) : Container(view, animateOnAttach = false) {
  
  private var paletteTool: GradientPalette = findViewById(R.id.palette)
  private var editText: EditText = findViewById(R.id.editTextExample)
  private var recyclerTextVariants: RecyclerView = findViewById(R.id.recyclerTextVariants)
  private var currentTextVariant: TextVariant = textVariantsList[0]
  
  private val textVariantsAdapter = ExampleTextsAdapter(onClick = {
    currentTextVariant = it
  })
  
  init {
    with(recyclerTextVariants) {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
      textVariantsAdapter.setHasStableIds(true)
      adapter = textVariantsAdapter
    }
    post {
      paletteTool.constraints {
        topMargin = topControlView.height * 2
      }
    }
    editText.setOnKeyListener { v, keyCode, event ->
      if (event.action == ACTION_DOWN && keyCode == KEYCODE_ENTER) {
        notifyTextEntered()
        return@setOnKeyListener true
      }
      return@setOnKeyListener false
    }
  }
  
  override fun animateEnter() {
    post {
      editText.requestFocus()
    }
  }
  
  override fun onBackPressed() {
    notifyTextEntered()
  }

  private fun notifyTextEntered() {
    if (editText.text.isNotBlank()) {
      onTextEntered(editText.text, currentTextVariant)
    }
  }
  
  class ExampleTextsAdapter(private val onClick: (TextVariant) -> Unit) :
    BaseRecyclerAdapter<TextVariant, ExampleTextsAdapter.ExampleTextVH>(textVariantsList) {
    
    private var selectedPosition = 0
    
    fun isSelected(position: Int): Boolean {
      return selectedPosition == position
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleTextVH {
      return ExampleTextVH(parent.inflate(R.layout.item_example_text))
    }
    
    inner class ExampleTextVH(itemView: View) : BaseViewHolder<TextVariant>(itemView) {
      
      init {
        itemView.setOnClickListener {
          val thisPosition = adapterPosition
          if (selectedPosition != thisPosition) {
            notifyItemChanged(selectedPosition)
            selectedPosition = thisPosition
            notifyItemChanged(selectedPosition)
            onClick(textVariantsList[adapterPosition])
          }
        }
      }
      
      override fun bind(item: TextVariant, position: Int) {
        itemView.textExample.draw(item)
        if (selectedPosition == position) {
          itemView.setBackgroundColor(Color.GRAY)
        } else {
          itemView.setBackgroundColor(Color.BLACK)
        }
      }
    }
  }
}