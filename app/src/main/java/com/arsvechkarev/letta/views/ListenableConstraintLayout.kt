package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class ListenableConstraintLayout @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {
  
  var listener: Listener? = null
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    listener?.onAttachedToWindow()
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    listener?.onDetachedFromWindow()
  }
  
  interface Listener {
    fun onAttachedToWindow() {}
    fun onDetachedFromWindow() {}
  }
}