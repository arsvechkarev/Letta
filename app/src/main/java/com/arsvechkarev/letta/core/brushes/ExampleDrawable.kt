package com.arsvechkarev.letta.core.brushes

import android.graphics.Canvas

interface ExampleDrawable {
  
  fun onExampleDraw(canvas: Canvas, x: Float, y: Float, brushWidth: Float)
}