package com.arsvechkarev.letta.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import com.arsvechkarev.letta.utils.f
import kotlin.math.abs
import kotlin.math.sqrt

class VerticalSeekbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
    private const val CORNER_RADIUS = 50f
    private const val LINE_TRIM = 50f
    private const val LINE_WIDTH = 11f
  }
  
  private var onPercentChangedAction: (Float) -> Unit = {}
  private val path = Path()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }
  private var lineLength = 0f
  
  private var currentY = 0f
  private var circle = Circle()
  
  fun onPercentChanged(block: (Float) -> Unit) {
    this.onPercentChangedAction = block
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    lineLength = h - CORNER_RADIUS * 2
    currentY = (lineLength + LINE_TRIM) - (0.2f * lineLength) // 20% from bottom
  }
  
  override fun onDraw(canvas: Canvas) {
    path.moveTo(0f, 0f)
    path.lineTo(width - CORNER_RADIUS, 0f)
    path.quadTo(width.f, 0f, width.f, CORNER_RADIUS)
    path.lineTo(width.f, height.f - CORNER_RADIUS)
    path.quadTo(width.f, height.f, width - CORNER_RADIUS, height.f)
    path.lineTo(0f, height.f)
    path.close()
    paint.setRectStyle()
    canvas.drawPath(path, paint)
    
    paint.setLineStyle()
    canvas.drawLine(width.f / 2, LINE_TRIM, width.f / 2, height - LINE_TRIM, paint)
    
    paint.color = Color.RED
    canvas.drawLine(width.f / 2, height - LINE_TRIM, width.f / 2, currentY, paint)
    
    paint.setCircleStyle()
    circle.set(width.f / 2, currentY, 20f)
    canvas.drawCircle(width.f / 2, currentY, 20f, paint)
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        notifyEvent(event)
        return true
      }
      ACTION_MOVE -> {
        notifyEvent(event)
        return true
      }
      ACTION_UP -> {
        return true
      }
    }
    return false
  }
  
  private fun notifyEvent(event: MotionEvent) {
    currentY = event.y.coerceIn(LINE_TRIM, height.f - LINE_TRIM)
    val percent = (lineLength + LINE_TRIM - currentY) / lineLength
    onPercentChangedAction(percent)
    invalidate()
  }
  
  private fun Paint.setRectStyle() {
    color = Color.WHITE
    style = Paint.Style.FILL
  }
  
  private fun Paint.setLineStyle() {
    color = Color.GRAY
    strokeWidth = LINE_WIDTH
    strokeCap = Paint.Cap.ROUND
  }
  
  private fun Paint.setCircleStyle() {
    style = Paint.Style.FILL
    color = Color.RED
  }
  
  class Circle {
    var x: Float = 0f
    var y: Float = 0f
    var radius: Float = 0f
    var color: Int = 0
    
    fun set(x: Float, y: Float, radius: Float, color: Int = 0) {
      this.x = x
      this.y = y
      this.radius = radius
      this.color = color
    }
    
    operator fun contains(pointF: PointF): Boolean {
      val absX = abs(pointF.x - x)
      val absY = abs(pointF.y - y)
      val distToCenter = sqrt(absX * absX + absY * absY)
      return distToCenter <= radius
    }
  }
  
}
