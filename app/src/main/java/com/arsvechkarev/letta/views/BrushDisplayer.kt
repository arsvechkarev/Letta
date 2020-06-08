package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.letta.core.COLOR_BORDER_DARK
import com.arsvechkarev.letta.extensions.dp
import com.arsvechkarev.letta.extensions.f

class BrushDisplayer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeWidth = 1.dp
    color = COLOR_BORDER_DARK
    maskFilter = BlurMaskFilter(2.dp, BlurMaskFilter.Blur.NORMAL)
  }
  private var radius = 0f
  private var isDrawing = true
  
  fun draw(color: Int, radius: Float) {
    isDrawing = true
    this.radius = radius
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
      canvas.drawCircle(x, y, radius, circlePaint)
      canvas.drawCircle(x, y, radius, strokePaint)
    }
  }
}