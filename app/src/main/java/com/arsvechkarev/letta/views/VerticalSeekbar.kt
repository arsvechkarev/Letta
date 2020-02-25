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
import androidx.annotation.FloatRange
import com.arsvechkarev.letta.graphics.STROKE_PAINT
import com.arsvechkarev.letta.graphics.VERY_LIGHT_GRAY
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.f
import kotlin.math.abs
import kotlin.math.sqrt

class VerticalSeekbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
    private val CORNER_RADIUS = 16.dp
    private val LINE_OFFSET = 30.dp
    private val LINE_WIDTH = 4.dp
  }
  
  private val colorThreshold = Color.parseColor("#999999")
  private val colorThresholdChannel = 0x99
  
  var onPercentChanged: (Float) -> Unit = {}
  var onUp: () -> Unit = {}
  
  private val path = Path()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }
  private var lineLength = 0f
  private var color = Color.RED
  
  private var currentY = 0f
  private var circle = Circle()
  
  fun updatePercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    val lineLength = height - LINE_OFFSET * 2
    val value = lineLength * percent
    currentY = height - value - LINE_OFFSET
    invalidate()
  }
  
  fun updateColorIfAllowed(color: Int) {
    if (colorChangeAllowed(color)) {
      this.color = color
      invalidate()
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    lineLength = h - CORNER_RADIUS * 2
    currentY = (lineLength + LINE_OFFSET) - (0.2f * lineLength) // 20% from bottom
    with(path) {
      moveTo((-1).dp, CORNER_RADIUS / 2)
      quadTo(0f, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS)
      lineTo(width - CORNER_RADIUS, CORNER_RADIUS)
      quadTo(width.f, CORNER_RADIUS, width.f, CORNER_RADIUS * 2)
      lineTo(width.f, height.f - CORNER_RADIUS * 2)
      quadTo(width.f, height.f - CORNER_RADIUS, width - CORNER_RADIUS, height.f - CORNER_RADIUS)
      lineTo(CORNER_RADIUS, height.f - CORNER_RADIUS)
      quadTo(0f, height.f - CORNER_RADIUS, (-1).dp, height.f - CORNER_RADIUS / 2)
      close()
    }
  }
  
  override fun onDraw(canvas: Canvas) {
    val halfWidth = width.f / 2
    val endLine = height - LINE_OFFSET
    
    paint.setPathStyle()
    canvas.drawPath(path, paint)
    canvas.drawPath(path, STROKE_PAINT)
  
    paint.setLineStyle()
    canvas.drawLine(halfWidth, LINE_OFFSET, halfWidth, endLine, paint)
    
    paint.color = color
    canvas.drawLine(halfWidth, endLine, halfWidth, currentY, paint)
    
    paint.setCircleStyle()
    circle.set(halfWidth, currentY, 20f)
    canvas.drawCircle(halfWidth, currentY, 20f, paint)
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
  
  private fun Paint.setPathStyle() {
    color = Color.WHITE
    style = Paint.Style.FILL
  }
  
  private fun Paint.setLineStyle() {
    color = VERY_LIGHT_GRAY
    strokeWidth = LINE_WIDTH
    strokeCap = Paint.Cap.ROUND
  }
  
  private fun Paint.setCircleStyle() {
    this.color = color
    style = Paint.Style.FILL
  }
  
  private fun colorChangeAllowed(color: Int): Boolean {
    val r = color and 0b00000000_11111111_00000000_00000000 shr 16
    val g = color and 0b00000000_00000000_11111111_00000000 shr 8
    val b = color and 0b00000000_00000000_00000000_11111111
    println("rgb = $r, $g, $b __ $colorThresholdChannel")
    return !(r > colorThresholdChannel && g > colorThresholdChannel && b > colorThresholdChannel)
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
