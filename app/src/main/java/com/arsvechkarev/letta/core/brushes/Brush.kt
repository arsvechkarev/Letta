package com.arsvechkarev.letta.core.brushes

import android.graphics.Canvas

abstract class Brush(
  val color: Int,
  val width: Float
) : ExampleDrawable {
  
  abstract fun onDown(x: Float, y: Float)
  
  abstract fun onMove(lastX: Float, lastY: Float, x: Float, y: Float)
  
  abstract fun onUp(x: Float, y: Float)
  
  abstract fun draw(canvas: Canvas)
}