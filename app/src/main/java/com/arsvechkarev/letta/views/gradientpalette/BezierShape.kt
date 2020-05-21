package com.arsvechkarev.letta.views.gradientpalette

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.arsvechkarev.letta.core.model.Circle
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
  
  fun drawVertical(canvas: Canvas, circle: Circle, bezierDistance: Float, bezierOffset: Float) {
    val x0 = circle.x + cos(angleRadians) * circle.radius - horizontalOffset
    val y0 = circle.y + sin(angleRadians) * circle.radius + verticalOffset
    
    val x1 = bezierDistance
    val y1 = circle.y - bezierOffset
    
    val x2 = bezierDistance
    val y2 = circle.y + bezierOffset
    
    val x3 = circle.x + cos(angleRadians) * circle.radius - horizontalOffset
    val y3 = circle.y - sin(angleRadians) * circle.radius - verticalOffset
    
    bezierPath.moveTo(x0, y0)
    bezierPath.cubicTo(x1, y1, x2, y2, x3, y3)
    bezierPath.close()
    canvas.drawPath(bezierPath, bgPaint)
    bezierPath.reset()
  }
  
  fun drawHorizontal(canvas: Canvas, circle: Circle, bezierDistance: Float, bezierOffset: Float) {
    val x0 = circle.x - cos(angleRadians) * circle.radius + horizontalOffset
    val y0 = circle.y + sin(angleRadians) * circle.radius + verticalOffset
    
    val x1 = circle.x + bezierOffset
    val y1 = bezierDistance
    
    val x2 = circle.x - bezierOffset
    val y2 = bezierDistance
    
    val x3 = circle.x + cos(angleRadians) * circle.radius - horizontalOffset
    val y3 = circle.y + sin(angleRadians) * circle.radius + verticalOffset
    
    bezierPath.moveTo(x0, y0)
    bezierPath.cubicTo(x1, y1, x2, y2, x3, y3)
    bezierPath.close()
    canvas.drawPath(bezierPath, bgPaint)
    bezierPath.reset()
  }
}
  