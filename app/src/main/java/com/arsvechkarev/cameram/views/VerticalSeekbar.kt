package com.arsvechkarev.cameram.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class VerticalSeekbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private var circleRadius = 0f
  private var lineWidth = 0f
  
  override fun onDraw(canvas: Canvas) {
  
  }

}