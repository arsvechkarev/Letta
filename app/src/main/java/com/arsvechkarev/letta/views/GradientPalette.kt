package com.arsvechkarev.letta.views

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.i

// TODO (2/20/2020): add custom attrs
@Suppress("MemberVisibilityCanBePrivate")
class GradientPalette @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null
) : View(context, attributeSet) {
  
  companion object {
    private val strokeWidthValue = 3.dp
    private const val GRADIENT_SENSITIVITY = 9
  }
  
  // Gradient palette stuff
  private val paletteColors = intArrayOf(
    "#FF0000".color,
    "#FF7F00".color,
    "#FFFF00".color,
    "#00FF00".color,
    "#005FE5".color,
    "#8B00FF".color
  )
  private val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val gradientRect = RectF()
  private val gradientPath = Path()
  private val gradientRegion = Region()
  private lateinit var gradientBitmap: Bitmap
  
  // Circle and animation stuff
  private val circleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
    this.strokeWidth = strokeWidthValue
    style = Paint.Style.STROKE
  }
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var rectRadius = 0f
  private var currentCircle = Circle()
  private var radiusSelected = 0f
  private var radiusFloating = 0f
  private var startAnimX = 0f
  private var endAnimX = 0f
  private var currentY = 0f
  private var currentAnimX = 0f
  private var currentAnimRadius = 0f
  private val circleAnimator = ValueAnimator()
  private val xHolder = PropertyValuesHolder.ofFloat("xHolder", 0f) // Put 0 as a stub
  private val radiusHolder = PropertyValuesHolder.ofFloat("radiusHolder", 0f) // Put 0 as a stub
  
  var onColorChanged: (Int) -> Unit = {}
  var currentColor = 0
    private set
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    gradientRect.set(paddingLeft.f, paddingTop.f + strokeWidthValue * 2,
      w.f - paddingEnd, h.f - paddingBottom - strokeWidthValue * 2)
    gradientPaint.shader = LinearGradient(
      gradientRect.width() / 2, gradientRect.top,
      gradientRect.width() / 2, gradientRect.bottom,
      paletteColors, null, Shader.TileMode.CLAMP
    )
    rectRadius = w / 2f
    startAnimX = w / 2f
    endAnimX = -w * 3f
    radiusFloating = w * 1.5f
    radiusSelected = width / 2f - strokeWidthValue / 2
    currentAnimX = startAnimX
    currentAnimRadius = radiusSelected
    currentCircle.set(w / 2f, h / 2f, radiusSelected)
    initBitmap(gradientRect.width().i, gradientRect.height().i)
    currentColor = gradientBitmap.getPixel(gradientRect.width().i / 2, h / 2)
    circlePaint.color = currentColor
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawBitmap(gradientBitmap, gradientRect.left, gradientRect.top, gradientPaint)
    currentCircle.draw(canvas, circlePaint)
    currentCircle.draw(canvas, circleStrokePaint)
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    currentY = event.y.coerceIn(gradientRect.top + GRADIENT_SENSITIVITY,
      gradientRect.bottom - GRADIENT_SENSITIVITY)
    currentCircle.y = currentY
    currentColor = gradientBitmap.getPixel(gradientRect.width().i / 2,
      (currentY - gradientRect.top).i)
    onColorChanged(currentColor)
    circlePaint.color = currentColor
    when (event.action) {
      ACTION_DOWN -> {
        updateAnimation()
        return true
      }
      ACTION_MOVE -> {
        invalidate()
        return true
      }
      ACTION_UP, ACTION_CANCEL -> {
        updateAnimation(true)
        return true
      }
    }
    return false
  }
  
  private fun updateAnimation(animateBack: Boolean = false) {
    currentCircle.set(width / 2f, currentY, radiusSelected)
    if (animateBack) {
      circleAnimator.cancel()
      xHolder.setFloatValues(currentAnimX, startAnimX)
      radiusHolder.setFloatValues(currentAnimRadius, radiusSelected)
      circleAnimator.setValues(xHolder, radiusHolder)
      kickInAnimator()
    } else {
      circleAnimator.cancel()
      xHolder.setFloatValues(currentAnimX, endAnimX)
      radiusHolder.setFloatValues(currentAnimRadius, radiusFloating)
      circleAnimator.setValues(xHolder, radiusHolder)
      kickInAnimator()
    }
  }
  
  private fun kickInAnimator() {
    circleAnimator.apply {
      addUpdateListener {
        currentAnimX = getAnimatedValue("xHolder") as Float
        currentAnimRadius = getAnimatedValue("radiusHolder") as Float
        currentCircle.set(currentAnimX, currentY, currentAnimRadius)
        invalidate()
      }
      interpolator = AccelerateDecelerateInterpolator()
      duration = 150
      start()
    }
  }
  
  private fun initBitmap(width: Int, height: Int) {
    gradientBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(gradientBitmap)
    gradientPath.addRoundRect(0f, 0f, gradientRect.width(), gradientRect.height(),
      gradientRect.width() / 2, gradientRect.width() / 2, Path.Direction.CW)
    gradientRegion.setPath(gradientPath, Region(gradientRect.toRect()))
    canvas.drawPath(gradientPath, gradientPaint)
  }
  
  class Circle {
    var radius = 0f
    var x = 0f
    var y = 0f
    
    fun set(x: Float, y: Float, radius: Float) {
      this.x = x
      this.y = y
      this.radius = radius
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
      canvas.drawCircle(x, y, radius, paint)
    }
  }
  
  private val String.color get() = Color.parseColor(this)
  
  private fun RectF.toRect(): Rect {
    return Rect(left.i, top.i, right.i, bottom.i)
  }
}
