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
import android.view.MotionEvent.ACTION_OUTSIDE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.utils.doOnEnd
import com.arsvechkarev.letta.utils.i
import com.arsvechkarev.letta.views.GradientPalette.Mode.ANIMATING
import com.arsvechkarev.letta.views.GradientPalette.Mode.FLOATING_CIRCLE
import com.arsvechkarev.letta.views.GradientPalette.Mode.SELECTED_CIRCLE

@Suppress("MemberVisibilityCanBePrivate")
class GradientPalette @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null
) : View(context, attributeSet) {
  
  private val paletteColors = intArrayOf(
    "#FF0000".color,
    "#FF7F00".color,
    "#FFFF00".color,
    "#00FF00".color,
    "#005FE5".color,
    "#8B00FF".color
  )
  
  companion object {
    const val strokeWidthValue = 10f
    const val gradientSensitivity = 8
  }
  
  private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val circleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
    this.strokeWidth = strokeWidthValue
    style = Paint.Style.STROKE
  }
  
  private lateinit var bgBitmap: Bitmap
  private val pathRect = RectF()
  
  private var rectRadius = 0f
  private val path = Path()
  private val region = Region()
  private var currentCircle = Circle()
  
  private var mode = SELECTED_CIRCLE
  
  private var startAnimX = 0f
  private var endAnimX = 0f
  private var radiusSelected = 0f
  private var radiusFloating = 0f
  private var currentAnimX = 0f
  private var currentAnimY = 0f
  private var currentAnimRadius = 0f
  private lateinit var xHolder: PropertyValuesHolder
  private lateinit var radiusHolder: PropertyValuesHolder
  private var circleAnimator = ValueAnimator()
  
  var onColorChanged: (Int) -> Unit = {}
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val left = paddingLeft.f
    val top = paddingTop.f + strokeWidthValue * 2
    val right = w.f - paddingEnd
    val bottom = h.f - paddingBottom - strokeWidthValue * 2
    pathRect.set(left, top, right, bottom)
    bgPaint.shader = LinearGradient(
      pathRect.width() / 2,
      pathRect.top,
      pathRect.width() / 2,
      pathRect.bottom,
      paletteColors,
      null,
      Shader.TileMode.CLAMP
    )
    rectRadius = w / 2f
    startAnimX = w / 2f
    endAnimX = -w * 3f
    radiusFloating = w * 1.5f
    radiusSelected = width / 2f - strokeWidthValue / 2
    currentCircle.set(w / 2f, h / 2f, radiusSelected)
    initBitmap(pathRect.width().i, pathRect.height().i)
    circlePaint.color = bgBitmap.getPixel(w / 2, h / 2)
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawBitmap(bgBitmap, pathRect.left, pathRect.top, bgPaint)
    currentCircle.draw(canvas, circlePaint)
    currentCircle.draw(canvas, circleStrokePaint)
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        mode = ANIMATING
        val y = event.y.coerceIn(pathRect.top + gradientSensitivity,
          pathRect.bottom - gradientSensitivity)
        val i = (y - pathRect.top).i
        currentCircle.y = y
        updateAnimation(y)
        val color = bgBitmap.getPixel(width / 2, i)
        onColorChanged(color)
        circlePaint.color = color
        return true
      }
      ACTION_MOVE -> {
        val y = event.y.coerceIn(pathRect.top + gradientSensitivity,
          pathRect.bottom - gradientSensitivity)
        val i = (y - pathRect.top).i
        println("aa_ pb = ${pathRect.bottom - pathRect.top}")
        println("aa_ pt = ${pathRect.top}")
        println("aa_ y = $y")
        println("aa_ yi = $i")
        println("aa_ h = $height")
        println("aa_ hb = ${bgBitmap.height}")
        currentCircle.y = y
        if (mode == ANIMATING) {
          updateAnimation(y)
        } else {
          val color = bgBitmap.getPixel(width / 2, i)
          onColorChanged(color)
          circlePaint.color = color
          invalidate()
        }
        return true
      }
      ACTION_UP, ACTION_CANCEL -> {
        mode = ANIMATING
        val y = event.y.coerceIn(pathRect.top + gradientSensitivity,
          pathRect.bottom - gradientSensitivity)
        currentCircle.y = y
        updateAnimation(y, true)
        return true
      }
    }
    return false
  }
  
  private fun updateAnimation(y: Float, animateBack: Boolean = false) {
    currentAnimY = y
    if (animateBack) {
      with(circleAnimator) {
        reverse()
        doOnEnd { mode = SELECTED_CIRCLE }
      }
    } else {
      if (circleAnimator.isRunning) {
        circlePaint.color = bgBitmap.getPixel(width / 2, currentAnimY.i - pathRect.top.i)
        currentCircle.y = currentAnimY
      } else {
        xHolder = PropertyValuesHolder.ofFloat("xHolder", startAnimX, endAnimX)
        radiusHolder =
            PropertyValuesHolder.ofFloat("radiusHolder", radiusSelected, radiusFloating)
        circleAnimator = ValueAnimator.ofPropertyValuesHolder(xHolder, radiusHolder)
        currentCircle.set(width / 2f, currentAnimY, radiusSelected)
        circleAnimator.apply {
          addUpdateListener {
            currentAnimX = getAnimatedValue("xHolder") as Float
            currentAnimRadius = getAnimatedValue("radiusHolder") as Float
            currentCircle.set(currentAnimX, currentAnimY, currentAnimRadius)
            invalidate()
          }
          interpolator = AccelerateDecelerateInterpolator()
          duration = 150
          start()
          doOnEnd { mode = FLOATING_CIRCLE }
        }
      }
    }
  }
  
  private fun initBitmap(width: Int, height: Int) {
    bgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bgBitmap)
    path.addRoundRect(0f, 0f, pathRect.width(), pathRect.height(), pathRect.width() / 2,
      pathRect.width() / 2, Path.Direction.CCW)
    region.setPath(path, Region(pathRect.toRect()))
    canvas.drawPath(path, bgPaint)
  }
  
  private enum class Mode {
    FLOATING_CIRCLE,
    SELECTED_CIRCLE,
    ANIMATING
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
  
  private val Int.f get() = toFloat()
  private val String.color get() = Color.parseColor(this)
  
  private fun RectF.toRect(): Rect {
    return Rect(left.i, top.i, right.i, bottom.i)
  }
}
