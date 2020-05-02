package com.arsvechkarev.letta.views.gradientpalette

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.cos
import kotlin.math.sin

class BezierShape {
  
  private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
  private val bezierPath = Path()
  private var angleRadians = 0f
  private var verticalOffset = 0f
  private var horizontalOffset = 0f
  
  fun init(angle: Float, verticalOffset: Float, horizontalOffset: Float) {
    angleRadians = angle
    this.verticalOffset = verticalOffset
    this.horizontalOffset = horizontalOffset
  }
  
  fun draw(canvas: Canvas, circle: Circle, triangleEnd: Float, bezierOffset: Float, circleX: Float, circleY: Float) {
    val x1 = (cos(angleRadians) * circle.radius + circleX) - horizontalOffset
    val y1 = (sin(angleRadians) * circle.radius + circleY) + verticalOffset
    
    val x3 = (cos(angleRadians) * circle.radius + circleX) - horizontalOffset
    val y3 = (-sin(angleRadians) * circle.radius + circleY) - verticalOffset
    
    with(bezierPath) {
      moveTo(x1, y1)
      cubicTo(
        triangleEnd + bezierOffset, circleY - bezierOffset,
        triangleEnd + bezierOffset, circleY + bezierOffset,
        x3, y3
      )
      close()
    }
    
    canvas.drawPath(bezierPath, bgPaint)
    bezierPath.reset()
  }
}
  