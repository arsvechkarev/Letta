package com.arsvechkarev.letta.editing

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.animations.fadeIn
import com.arsvechkarev.letta.animations.fadeOut
import com.arsvechkarev.letta.graphics.TextVariant
import com.arsvechkarev.letta.graphics.textVariantsList
import com.arsvechkarev.letta.utils.extenstions.constraints
import com.arsvechkarev.letta.views.ExtendedEditText
import com.arsvechkarev.letta.views.GradientPalette
import com.arsvechkarev.letta.views.ListenableConstraintLayout

class TextContainer(
  view: ListenableConstraintLayout,
  private val topControlView: View,
  private val onTextEntered: (CharSequence, TextVariant) -> Unit
) : Container(view) {
  
  private var paletteTool: GradientPalette = findViewById(R.id.palette)
  private var editText: ExtendedEditText = findViewById(R.id.editTextExample)
  private var recyclerTextVariants: RecyclerView = findViewById(R.id.recyclerTextVariants)
  private var currentTextVariant: TextVariant = textVariantsList[0]
  
  private val textVariantsAdapter = TextVariantsAdapter(onClick = {
    currentTextVariant = it
  })
  
  init {
    recyclerTextVariants.layoutManager = LinearLayoutManager(view.context, HORIZONTAL, false)
    recyclerTextVariants.adapter = textVariantsAdapter
    editText.onBackPressed = { onTextEntered(it, currentTextVariant) }
  }
  
  override fun onEnter() {
    post {
      paletteTool.constraints { topMargin = topControlView.height * 2 }
      editText.requestFocus()
      toggleKeyboard()
    }
  }
  
  override fun onExit(andThen: () -> Unit) {
    editText.clearFocus()
    paletteTool.fadeOut()
    editText.fadeOut()
    recyclerTextVariants.fadeOut()
    andThen()
  }
  
  fun onKeyboardOpened() {
    post {
      paletteTool.fadeIn()
      editText.fadeIn()
      recyclerTextVariants.fadeIn()
    }
  }
}