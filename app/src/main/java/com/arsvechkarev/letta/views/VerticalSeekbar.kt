package com.arsvechkarev.letta.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.STROKE_PAINT
import com.arsvechkarev.letta.extensions.dp
import com.arsvechkarev.letta.extensions.f

class VerticalSeekbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val backgroundColor = ContextCompat.getColor(context, R.color.light_background)
  private val path = Path()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }
  
  private var cornersRadius = 0f
  private var lineVerticalOffset = 0f
  private var lineLength = 0f
  private var lineWidth = 0f
  private var color = Color.RED
  private var currentY = 0f
  
  var onPercentChanged: (Float) -> Unit = {}
  var onUp: () -> Unit = {}
  
  fun updatePercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    post {
      val lineLength = height - lineVerticalOffset * 2
      val value = lineLength * percent
      currentY = height - value - lineVerticalOffset
      invalidate()
    }
  }
  
  fun updateColorIfAllowed(color: Int) {
    this.color = color.changeIfNecessary()
    invalidate()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    lineLength = h - cornersRadius * 2
    lineWidth = w / 6.5f
    lineVerticalOffset = h / 10f
    cornersRadius = w / 2f
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
    canvas.drawCircle(halfWidth, currentY, halfWidth / 2f, paint)
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
    color = Colors.BorderVeryLight
    strokeWidth = lineWidth
    strokeCap = Paint.Cap.ROUND
  }
  
  private fun Paint.setCircleStyle() {
    this.color = color
    style = Paint.Style.FILL
  }
  
  private fun Int.changeIfNecessary(): Int {
    val r = this and 0x00FF0000 shr 16
    val g = this and 0x0000FF00 shr 8
    val b = this and 0x000000FF
    val onBlackAndWhiteScale = r == g && r == b
    if (onBlackAndWhiteScale) {
      val maxChannel = 0x88
      val maxR = r.coerceAtMost(maxChannel)
      val maxG = g.coerceAtMost(maxChannel)
      val maxB = b.coerceAtMost(maxChannel)
      return (0xFF shl 24) or (maxR shl 16) or (maxG shl 8) or maxB
    } else {
      return this
    }
  }
}
