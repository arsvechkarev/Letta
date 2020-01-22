package com.arsvechkarev.cameram.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import com.arsvechkarev.cameram.utils.f
import com.arsvechkarev.cameram.utils.toPointF
import com.arsvechkarev.cameram.views.common.Circle

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
  
  private val path = Path()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }
  private var lineLength = 0f
  
  private var allowMoving = false
  private var progress = 0.3f
  private var currentY = 0f
  private var circle = Circle()
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    lineLength = h - CORNER_RADIUS * 2
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
    val progressPoint = height.f - (lineLength * progress)
    canvas.drawLine(width.f / 2, height - LINE_TRIM, width.f / 2, progressPoint, paint)
    
    paint.setCircleStyle()
    canvas.drawCircle(width.f / 2, progressPoint, 20f, paint)
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        val point = event.toPointF()
        if (point in circle) {
          allowMoving = true
        }
        return true
      }
      ACTION_MOVE -> {
        if (allowMoving) {
          currentY = event.y.coerceIn(LINE_TRIM, lineLength + LINE_TRIM)
          invalidate()
          return true
        }
      }
      ACTION_UP -> {
        allowMoving = false
        return true
      }
    }
    return false
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
  
}
