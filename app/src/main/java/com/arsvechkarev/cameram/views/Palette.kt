package com.arsvechkarev.cameram.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.BLACK
import android.graphics.Color.BLUE
import android.graphics.Color.GRAY
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.graphics.Color.TRANSPARENT
import android.graphics.Color.WHITE
import android.graphics.Color.YELLOW
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Style.FILL
import android.graphics.Path
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.arsvechkarev.cameram.R
import com.arsvechkarev.cameram.utils.f
import com.arsvechkarev.cameram.utils.toBitmap
import com.arsvechkarev.cameram.utils.toPointF
import com.arsvechkarev.cameram.views.common.Circle


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
  private val paint = Paint(ANTI_ALIAS_FLAG).apply {
    style = FILL
    color = WHITE
  }
  // Creating array of empty circles that will be filled later
  private val circles = Array(NUMBER_OF_CIRCLES) { Circle() }
  private var circleDiameter = 0f
  private var circleDistance = 0f
  
  private val checkmark: Bitmap
  private val rectOfBitmap = RectF()
  private var selectedCircleColor = 0
  
  private var onColorSelectedAction: (Int) -> Unit = {}
  
  init {
    setBackgroundColor(TRANSPARENT)
    checkmark = ContextCompat.getDrawable(context, R.drawable.ic_checkmark)!!.toBitmap()
  }
  
  fun onColorSelected(block: (Int) -> Unit) {
    this.onColorSelectedAction = block
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    circleDiameter = w / 2f
    circleDistance = (h - (NUMBER_OF_CIRCLES * circleDiameter)) / (NUMBER_OF_CIRCLES + 1)
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      for (circle in circles) {
        if (event.toPointF() in circle) {
          val radius = circleDiameter / 2
          selectedCircleColor = circle.color
          rectOfBitmap.set(
            circle.x - radius,
            circle.y - radius,
            circle.x + radius,
            circle.y + radius
          )
          onColorSelectedAction(circle.color)
          invalidate()
          return true
        }
      }
    }
    return false
  }
  
  override fun onDraw(canvas: Canvas) {
    path.moveTo(width.f, 0f)
    path.lineTo(CORNER_RADIUS, 0f)
    path.quadTo(0f, 0f, 0f, CORNER_RADIUS)
    path.lineTo(0f, height.f - CORNER_RADIUS)
    path.quadTo(0f, height.f, CORNER_RADIUS, height.f)
    path.lineTo(width.f, height.f)
    path.close()
    paint.setRectStyle()
    canvas.drawPath(path, paint)
    
    val x = circleDiameter
    var y = circleDistance + circleDiameter / 2
    for (i in 1..NUMBER_OF_CIRCLES) {
      paint.setCircleStyle(i)
      canvas.drawCircle(x, y, circleDiameter / 2, paint)
      circles[i - 1].set(x, y, circleDiameter / 2, paint.color)
      paint.setStrokeStyle()
      canvas.drawCircle(x, y, circleDiameter / 2, paint)
      y += circleDistance + circleDiameter
    }
    paint.setCheckmarkStyle(selectedCircleColor)
    canvas.drawBitmap(checkmark, null, rectOfBitmap, paint)
  }
  
  private fun Paint.setRectStyle() {
    reset()
    this.color = WHITE
    this.style = FILL
  }
  
  private fun Paint.setCircleStyle(i: Int) {
    reset()
    with(this) {
      style = FILL
      color = when (i) {
        1 -> WHITE
        2 -> BLACK
        3 -> BLUE
        4 -> GRAY
        5 -> GREEN
        6 -> RED
        7 -> YELLOW
        else -> error("Unknown color for position $i")
      }
    }
  }
  
  private fun Paint.setStrokeStyle() {
    reset()
    with(this) {
      style = Paint.Style.STROKE
      strokeWidth = CIRCLE_BORDER_WIDTH
      color = GRAY
    }
  }
  
  private fun Paint.setCheckmarkStyle(circleColor: Int) {
    reset()
    colorFilter = if (circleColor == WHITE || circleColor == YELLOW) {
      PorterDuffColorFilter(BLACK, SRC_ATOP)
    } else {
      PorterDuffColorFilter(WHITE, SRC_ATOP)
    }
    this.style = FILL
  }
  
}