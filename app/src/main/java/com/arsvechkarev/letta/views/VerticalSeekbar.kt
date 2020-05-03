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
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.COLOR_BORDER_VERY_LIGHT
import com.arsvechkarev.letta.core.STROKE_PAINT
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.isWhiterThan
import kotlin.math.abs
import kotlin.math.sqrt

class VerticalSeekbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val cornersRadius: Float
  private val lineVerticalOffset: Float
  private val lineWidth: Float
  
  private val backgroundColor: Int
  
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
  
  init {
    val attributes = context.obtainStyledAttributes(attrs, R.styleable.VerticalSeekbar,
      defStyleAttr, 0)
    backgroundColor = context.getColor(R.color.background)
    cornersRadius = attributes.getDimension(R.styleable.VerticalSeekbar_cornersRadius, 16.dp)
    lineVerticalOffset = attributes.getDimension(R.styleable.VerticalSeekbar_lineVerticalOffset,
      30.dp)
    lineWidth = attributes.getDimension(R.styleable.VerticalSeekbar_lineWidth, 4.dp)
    attributes.recycle()
  }
  
  fun updatePercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    val lineLength = height - lineVerticalOffset * 2
    val value = lineLength * percent
    currentY = height - value - lineVerticalOffset
    invalidate()
  }
  
  fun updateColorIfAllowed(color: Int) {
    if (colorChangeAllowed(color)) {
      this.color = color
      invalidate()
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    lineLength = h - cornersRadius * 2
    currentY = (lineLength + lineVerticalOffset) - (0.2f * lineLength) // 20% from bottom
    with(path) {
      moveTo((-1).dp, cornersRadius / 2)
      quadTo(0f, cornersRadius, cornersRadius, cornersRadius)
      lineTo(width - cornersRadius, cornersRadius)
      quadTo(width.f, cornersRadius, width.f, cornersRadius * 2)
      lineTo(width.f, height.f - cornersRadius * 2)
      quadTo(width.f, height.f - cornersRadius, width - cornersRadius, height.f - cornersRadius)
      lineTo(cornersRadius, height.f - cornersRadius)
      quadTo(0f, height.f - cornersRadius, (-1).dp, height.f - cornersRadius / 2)
      close()
    }
  }
  
  override fun onDraw(canvas: Canvas) {
    val halfWidth = width.f / 2
    val endLine = height - lineVerticalOffset
    
    paint.setPathStyle()
    canvas.drawPath(path, paint)
    canvas.drawPath(path, STROKE_PAINT)
    
    paint.setLineStyle()
    canvas.drawLine(halfWidth, lineVerticalOffset, halfWidth, endLine, paint)
    
    paint.color = color
    canvas.drawLine(halfWidth, endLine, halfWidth, currentY, paint)
    
    paint.setCircleStyle()
    circle.set(halfWidth, currentY, width / 4f)
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
    currentY = event.y.coerceIn(lineVerticalOffset, height.f - lineVerticalOffset)
    val percent = (lineLength + lineVerticalOffset - currentY) / lineLength
    onPercentChanged(percent)
    invalidate()
  }
  
  private fun Paint.setPathStyle() {
    color = backgroundColor
    style = Paint.Style.FILL
  }
  
  private fun Paint.setLineStyle() {
    color = COLOR_BORDER_VERY_LIGHT
    strokeWidth = lineWidth
    strokeCap = Paint.Cap.ROUND
  }
  
  private fun Paint.setCircleStyle() {
    this.color = color
    style = Paint.Style.FILL
  }
  
  private fun colorChangeAllowed(color: Int): Boolean {
    return !color.isWhiterThan(0xAA)
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
