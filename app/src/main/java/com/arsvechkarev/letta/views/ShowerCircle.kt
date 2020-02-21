package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.letta.utils.f

class ShowerCircle @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var diameter = 0f
  private var isDrawing = false
  
  fun draw(color: Int, diameter: Float) {
    isDrawing = true
    this.diameter = diameter
    paint.color = color
    invalidate()
  }
  
  fun erase() {
    isDrawing = false
    invalidate()
  }
  
  override fun onDraw(canvas: Canvas) {
    if (isDrawing) {
      canvas.drawCircle(width.f / 2, height.f / 4, diameter / 2, paint)
    }
  }
  
}