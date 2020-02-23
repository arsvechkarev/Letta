package com.arsvechkarev.letta.editing

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.TextVariant
import com.arsvechkarev.letta.core.examplesTextList
import com.arsvechkarev.letta.utils.inflate
import com.arsvechkarev.letta.views.DrawingCanvas
import com.arsvechkarev.letta.views.GradientPalette
import kotlinx.android.synthetic.main.item_example_text.view.textExample

class TextContainer(
  view: View,
  private val drawingCanvas: DrawingCanvas,
  private val topControlView: View
) : Container(view) {
  
  private var paletteTool: GradientPalette = findViewById(R.id.palette)
  private var editText: EditText = findViewById(R.id.editTextExample)
  private var recyclerExampleTexts: RecyclerView = findViewById(R.id.recyclerExampleTexts)
  
  init {
    recyclerExampleTexts.layoutManager =
      LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
    recyclerExampleTexts.adapter = ExampleTextsAdapter()
  }
  
  override fun animateEnter() {
    post {
      editText.requestFocus()
      toggleKeyboard()
    }
  }
  
  class ExampleTextsAdapter : RecyclerView.Adapter<ExampleTextsAdapter.ExampleTextVH>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleTextVH {
      return ExampleTextVH(parent.inflate(R.layout.item_example_text))
    }
    
    override fun getItemCount(): Int {
      return examplesTextList.size
    }
    
    override fun onBindViewHolder(holder: ExampleTextVH, position: Int) {
      holder.bind(examplesTextList[position])
    }
    
    class ExampleTextVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
      
      fun bind(textVariant: TextVariant) {
        itemView.textExample.draw(textVariant)
      }
    }
  }
}