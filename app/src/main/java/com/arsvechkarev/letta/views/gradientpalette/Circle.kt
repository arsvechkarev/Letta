package com.arsvechkarev.letta.views.gradientpalette

import android.graphics.Canvas
import android.graphics.Paint

class Circle {
    var radius = 0f
    var x = 0f
    var y = 0f
    
    fun set(x: Float, y: Float, radius: Float) {
      this.x = x
      this.y = y
      this.radius = radius
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
      canvas.drawCircle(x, y, radius, paint)
    }
    
    fun drawInnerStroke(canvas: Canvas, strokePaint: Paint) {
      canvas.drawCircle(x, y, radius, strokePaint)
    }
    
    fun drawStroke(canvas: Canvas, strokePaintWidth: Float, paint: Paint) {
      canvas.drawCircle(x, y, radius + strokePaintWidth / 2, paint)
    }
  }