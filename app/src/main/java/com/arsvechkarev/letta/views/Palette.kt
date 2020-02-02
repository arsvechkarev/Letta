package com.arsvechkarev.letta.views

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
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
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.addListener
import com.arsvechkarev.letta.utils.f


class Palette @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
    private const val SELECTION_ANIMATION_DURATION = 300L
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
  private val circles = Array(NUMBER_OF_CIRCLES) { PaletteCircle() }
  private var circleX: Float = 0f
  private var circleDiameter = 0f
  private var circleIncreasedDiameter = 0f
  private var circleDistance = 0f
  
  private var previousCircle = PaletteCircle()
  private var currentCircle = PaletteCircle()
  
  private var updateSelectedColors = false
  private var currentIncreasingRadius = 0f
  private var currentDecreasingRadius = 0f
  
  private var onColorSelectedAction: (Int) -> Unit = {}
  
  init {
    setBackgroundColor(TRANSPARENT)
    setInitialColor(6)
  }
  
  fun onColorSelected(block: (Int) -> Unit) {
    this.onColorSelectedAction = block
  }
  
  fun setInitialColor(colorIndex: Int) {
    updateSelectedColors = true
    paint.setCircleStyle(colorIndex)
    previousCircle = PaletteCircle()
    var y = circleDistance + circleDiameter / 2
    when (colorIndex) {
      1 -> previousCircle.set(y, paint.color)
      in 2..7 -> {
        y += (circleDistance + circleDiameter) * colorIndex
        previousCircle.set(y, paint.color)
      }
      else -> error("Unknown color for position $colorIndex")
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    circleX = w / 2f
    circleDiameter = w / 2.3f
    circleIncreasedDiameter = w / 2f
    circleDistance = (h - (NUMBER_OF_CIRCLES * circleDiameter)) / (NUMBER_OF_CIRCLES + 1)
    var segmentBottom = 0f
    val segmentBottomIncrease = h / NUMBER_OF_CIRCLES.f
    circles.forEach {
      segmentBottom += segmentBottomIncrease
      it.segmentBorder = segmentBottom
    }
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      for (circle in circles) {
        if (event.y in circle) {
          animateCircle(circle)
          onColorSelectedAction(circle.color)
          invalidate()
          return true
        }
      }
    }
    return false
  }
  
  override fun onDraw(canvas: Canvas) {
    with(path) {
      moveTo(width.f, 0f)
      lineTo(CORNER_RADIUS, 0f)
      quadTo(0f, 0f, 0f, CORNER_RADIUS)
      lineTo(0f, height.f - CORNER_RADIUS)
      quadTo(0f, height.f, CORNER_RADIUS, height.f)
      lineTo(width.f, height.f)
      close()
    }
    paint.setRectStyle()
    canvas.drawPath(path, paint)
    
    var y = circleDistance + circleDiameter / 2
    for (i in 1..NUMBER_OF_CIRCLES) {
      paint.setCircleStyle(i)
      canvas.drawCircle(circleX, y, circleDiameter / 2, paint)
      circles[i - 1].set(y, paint.color)
      paint.setStrokeStyle()
      canvas.drawCircle(circleX, y, circleDiameter / 2, paint)
      y += circleDistance + circleDiameter
    }
    
    if (updateSelectedColors) {
      paint.reset()
      paint.style = FILL
      paint.color = previousCircle.color
      canvas.drawCircle(circleX, previousCircle.y, currentDecreasingRadius, paint)
      if (currentCircle.isInitialized) {
        paint.color = currentCircle.color
        canvas.drawCircle(circleX, currentCircle.y, currentIncreasingRadius, paint)
      }
    }
  }
  
  private fun animateCircle(circle: PaletteCircle) {
    val holderInc = PropertyValuesHolder.ofFloat("inc", circleDiameter, circleIncreasedDiameter)
    val holderDec = PropertyValuesHolder.ofFloat("dec", circleIncreasedDiameter, circleDiameter)
    updateSelectedColors = true
    ValueAnimator.ofPropertyValuesHolder(holderInc, holderDec).apply {
      addUpdateListener {
        currentIncreasingRadius = it.getAnimatedValue("inc") as Float
        currentDecreasingRadius = it.getAnimatedValue("dec") as Float
        invalidate()
      }
      addListener(onEnd = {
        currentCircle = circle
      })
      start()
    }
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
  
  class PaletteCircle {
    var y: Float = 0f
    var color: Int = 0
    var segmentBorder = 0f
    
    val isInitialized: Boolean
      get() = y != 0f
    
    fun set(y: Float, color: Int) {
      this.y = y
      this.color = color
    }
    
    operator fun contains(y: Float): Boolean {
      return y <= segmentBorder
    }
    
    override fun toString(): String {
      return "Circle2(y=$y, color=$color, segmentBorder=$segmentBorder)"
    }
    
  }
}