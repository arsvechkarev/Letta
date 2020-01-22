package com.arsvechkarev.cameram.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.arsvechkarev.cameram.R
import com.arsvechkarev.cameram.extensions.f
import com.arsvechkarev.cameram.extensions.toPointF
import kotlin.math.abs
import kotlin.math.sqrt

class Palette @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
    private const val CORNER_RADIUS = 50f
    private const val NUMBER_OF_CIRCLES = 7
    private const val CIRCLE_BORDER_WIDTH = 2f
  }
  
  private val path = Path()
  private val bitmapPaint = Paint(ANTI_ALIAS_FLAG)
  private val paint = Paint(ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
    color = Color.WHITE
  }
  // Creating array of empty circles that will be filled later
  private val circles = Array(NUMBER_OF_CIRCLES) { Circle() }
  private val bitmap: Bitmap
  
  private var circleSegmentHeight = 0f
  private var circleDiameter = 0f
  private var circleDistance = 0f
  
  // Draw checkmark at the center of a circle that user clicks on
  private var drawCheckMarkOnCircle = false
  private var clickedCircle = Circle()
  private val clickedCircleRect = RectF()
  
  private var onColorSelectedAction: (Int) -> Unit = {}
  
  init {
    setBackgroundColor(Color.TRANSPARENT)
    val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_checkmark)!!
    bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.draw(canvas)
  }
  
  fun onColorSelected(block: (Int) -> Unit) {
    this.onColorSelectedAction = block
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    circleDiameter = w / 2f
    circleDistance = (h - (NUMBER_OF_CIRCLES * circleDiameter)) / (NUMBER_OF_CIRCLES + 1)
    circleSegmentHeight = h / NUMBER_OF_CIRCLES.f
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      for (circle in circles) {
        if (event.toPointF() in circle) {
          drawCheckMarkOnCircle = true
          clickedCircle = circle
          val left = circle.x - circle.radius
          val top = circle.y - circle.radius
          val right = circle.x + circle.radius
          val bottom = circle.y + circle.radius
          clickedCircleRect.set(left, top, right, bottom)
          onColorSelectedAction(circle.color)
          invalidate()
          return true
        }
      }
    }
    return false
  }
  
  override fun onDraw(canvas: Canvas) {
    if (drawCheckMarkOnCircle) {
      canvas.drawBitmap(bitmap, null, clickedCircleRect, bitmapPaint)
      drawCheckMarkOnCircle = false
    }
    drawRoundRectangle(canvas)
    val x = circleDiameter
    var y = circleDistance + circleDiameter / 2
    for (i in 1..NUMBER_OF_CIRCLES) {
      setupCirclePaint(i)
      canvas.drawCircle(x, y, circleDiameter / 2, paint)
      circles[i - 1].set(x, y, circleDiameter / 2, paint.color)
      setupStrokePaint()
      canvas.drawCircle(x, y, circleDiameter / 2, paint)
      y += circleDistance + circleDiameter
    }
  }
  
  private fun drawRoundRectangle(canvas: Canvas) {
    path.moveTo(width.f, 0f)
    path.lineTo(CORNER_RADIUS, 0f)
    path.quadTo(0f, 0f, 0f, CORNER_RADIUS)
    path.lineTo(0f, height.f - CORNER_RADIUS)
    path.quadTo(0f, height.f, CORNER_RADIUS, height.f)
    path.lineTo(width.f, height.f)
    path.close()
    
    canvas.drawPath(path, paint)
  }
  
  private fun setupCirclePaint(i: Int) {
    with(paint) {
      style = Paint.Style.FILL
      color = when (i) {
        1 -> Color.WHITE
        2 -> Color.BLACK
        3 -> Color.BLUE
        4 -> Color.GRAY
        5 -> Color.GREEN
        6 -> Color.RED
        7 -> Color.YELLOW
        else -> error("Unknown color for position $i")
      }
    }
  }
  
  private fun setupStrokePaint() {
    with(paint) {
      style = Paint.Style.STROKE
      strokeWidth = CIRCLE_BORDER_WIDTH
      color = Color.GRAY
    }
  }
  
  class Circle {
    var x: Float = 0f
    var y: Float = 0f
    var radius: Float = 0f
    var color: Int = 0
    
    fun set(x: Float, y: Float, radius: Float, color: Int) {
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