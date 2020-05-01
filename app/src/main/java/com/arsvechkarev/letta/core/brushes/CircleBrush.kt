package com.arsvechkarev.letta.core.brushes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class CircleBrush(
  color: Int,
  width: Float
) : Brush(color, width) {
  
  private val path = Path()
  private var startedX = 0f
  private var startedY = 0f
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    this.color = color
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
    strokeWidth = width
  }
  
  override fun onDown(x: Float, y: Float) {
    this.startedX = x
    this.startedY = y
    path.moveTo(x, y)
    path.lineTo(x, y)
  }
  
  override fun onMove(lastX: Float, lastY: Float, x: Float, y: Float) {
    path.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2)
  }
  
  override fun onUp(x: Float, y: Float) {
    path.lineTo(x, y)
  }
  
  override fun draw(canvas: Canvas) {
    paint.style = Paint.Style.STROKE
    canvas.drawPath(path, paint)
  }
  
  override fun onExampleDraw(canvas: Canvas, x: Float, y: Float, brushWidth: Float) {
    paint.style = Paint.Style.FILL
    canvas.drawCircle(x, y, brushWidth / 2, paint)
  }
}