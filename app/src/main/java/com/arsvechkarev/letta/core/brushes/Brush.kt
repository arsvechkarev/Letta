package com.arsvechkarev.letta.core.brushes

import android.graphics.Canvas

abstract class Brush(
  var color: Int,
  brushSize: Float
) {
  
  var brushSize: Float = brushSize
    set(value) {
      field = value
      onBrushSizeChanged(value)
    }
  
  open fun onBrushSizeChanged(brushSize: Float) {}
  
  abstract val type: BrushType
  
  abstract fun onDown(x: Float, y: Float)
  
  abstract fun onMove(lastX: Float, lastY: Float, x: Float, y: Float)
  
  abstract fun onUp(x: Float, y: Float)
  
  abstract fun draw(canvas: Canvas)
  
  abstract fun onExampleDraw(canvas: Canvas, x: Float, y: Float, brushSize: Float)
}