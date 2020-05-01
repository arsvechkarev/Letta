package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.f

class BrushDisplayer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeWidth = 1.dp
    maskFilter = BlurMaskFilter(2.dp, BlurMaskFilter.Blur.NORMAL)
  }
  private var diameter = 0f
  private var isDrawing = true
  
  fun draw(color: Int, diameter: Float) {
    isDrawing = true
    this.diameter = diameter
    circlePaint.color = color
    invalidate()
  }
  
  fun clear() {
    isDrawing = false
    invalidate()
  }
  
  override fun onDraw(canvas: Canvas) {
    if (isDrawing) {
      val x = width.f / 2
      val y = height.f / 4
      val radius = diameter / 2
      canvas.drawCircle(x, y, radius, circlePaint)
      canvas.drawCircle(x, y, radius, strokePaint)
    }
  }
}