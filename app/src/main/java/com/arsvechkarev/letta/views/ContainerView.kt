package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class ContainerView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  var listener: EventListener? = null
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    listener?.onAttachedToWindow()
  }
  
  interface EventListener {
    
    fun onAttachedToWindow() {}
  }
}