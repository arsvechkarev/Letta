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
import android.view.MotionEvent.ACTION_CANCEL
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
    private const val LINE_OFFSET = 100f
    private const val LINE_WIDTH = 11f
  }
  
  var onPercentChanged: (Float) -> Unit = {}
  var onUp: () -> Unit = {}
  
  private val path = Path()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }
  private var lineLength = 0f
  
  private var currentY = 0f
  private var circle = Circle()
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    lineLength = h - CORNER_RADIUS * 2
    currentY = (lineLength + LINE_OFFSET) - (0.2f * lineLength) // 20% from bottom
    with(path) {
      moveTo(0f, CORNER_RADIUS / 2)
      quadTo(0f, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS)
      lineTo(width - CORNER_RADIUS, CORNER_RADIUS)
      quadTo(width.f, CORNER_RADIUS, width.f, CORNER_RADIUS * 2)
      lineTo(width.f, height.f - CORNER_RADIUS * 2)
      quadTo(width.f, height.f - CORNER_RADIUS, width - CORNER_RADIUS, height.f - CORNER_RADIUS)
      lineTo(CORNER_RADIUS, height.f - CORNER_RADIUS)
      quadTo(0f, height.f - CORNER_RADIUS, 0f, height.f - CORNER_RADIUS / 2)
      close()
    }
  }
  
  override fun onDraw(canvas: Canvas) {
    paint.setRectStyle()
    canvas.drawPath(path, paint)
    
    paint.setLineStyle()
    canvas.drawLine(width.f / 2, LINE_OFFSET, width.f / 2, height - LINE_OFFSET, paint)
    
    paint.color = Color.RED
    canvas.drawLine(width.f / 2, height - LINE_OFFSET, width.f / 2, currentY, paint)
    
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
      ACTION_UP, ACTION_CANCEL -> {
        onUp()
        return true
      }
    }
    return false
  }
  
  private fun notifyEvent(event: MotionEvent) {
    currentY = event.y.coerceIn(LINE_OFFSET, height.f - LINE_OFFSET)
    val percent = (lineLength + LINE_OFFSET - currentY) / lineLength
    onPercentChanged(percent)
    invalidate()
  }
  
  private fun Paint.setRectStyle() {
    color = Color.WHITE
    style = Paint.Style.FILL
  }
  
  private fun Paint.setLineStyle() {
    color = -3355444 // #CCCCCC
    strokeWidth = LINE_WIDTH
    strokeCap = Paint.Cap.ROUND
  }
  
  private fun Paint.setCircleStyle() {
    color = Color.RED
    style = Paint.Style.FILL
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
