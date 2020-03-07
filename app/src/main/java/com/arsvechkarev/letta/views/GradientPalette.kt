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
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.animations.addBouncyBackEffect
import com.arsvechkarev.letta.graphics.LIGHT_GRAY
import com.arsvechkarev.letta.graphics.STROKE_PAINT
import com.arsvechkarev.letta.graphics.STROKE_PAINT_LIGHT
import com.arsvechkarev.letta.utils.extenstions.doOnEnd
import com.arsvechkarev.letta.utils.extenstions.dp
import com.arsvechkarev.letta.utils.extenstions.execute
import com.arsvechkarev.letta.utils.extenstions.f
import com.arsvechkarev.letta.utils.extenstions.i
import com.arsvechkarev.letta.utils.extenstions.isWhiterThan
import com.arsvechkarev.letta.utils.extenstions.toBitmap
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// TODO (2/20/2020): add custom attrs
@Suppress("MemberVisibilityCanBePrivate")
class GradientPalette @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
    private val circleStrokeWidth = 7.dp
    private const val GRADIENT_SENSITIVITY = 2
    
    private const val CIRCLE_ANIMATION_DURATION = 150L
    private const val SWAP_ANIMATION_DURATION = 200L
  }
  
  private val gradientStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    strokeWidth = 7.dp
    color = Color.WHITE
    style = Paint.Style.STROKE
  }
  private val gradientPaint = Paint()
  private val gradientRect = RectF()
  private val gradientPath = Path()
  private val gradientRegion = Region()
  private lateinit var rainbowGradient: LinearGradient
  private lateinit var blackAndWhiteGradient: LinearGradient
  private lateinit var gradientBitmap: Bitmap
  private var isTouchInPalette = false
  private var currentGradientScale = 1f
  
  // Circle and animation
  private val circleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
    this.strokeWidth = circleStrokeWidth
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
  
  // Swapper
  private var swapperMode = SwapperMode.RAINBOW
  private var currentSwapperRotation = 0f
  private val swapperBitmap = ContextCompat.getDrawable(context, R.drawable.ic_swap)!!.toBitmap()
  private val swapperBitmapStroke = ContextCompat.getDrawable(context,
    R.drawable.ic_swap_stroke)!!.toBitmap()
  private val swapperPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
  }
  private val swapperPaintStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(LIGHT_GRAY, PorterDuff.Mode.SRC_ATOP)
  }
  private val swapperRect = RectF()
  private val swapperAnimator = ValueAnimator()
  private val bouncyAnimator = ValueAnimator()
  
  // Bezier path
  private val bezierShape = BezierShape()
  private var bezierSpotStart = 0f
  private var bezierSpotEnd = 0f
  private var bezierSpotValue = 0f
  private val bezierHolder = PropertyValuesHolder.ofFloat("bezierHolder", 0f) // Put 0 as a stub
  private var bezierSpotOffset = 22.dp
  private var bezierVerticalOffset = 10.dp
  private var bezierHorizontalOffset = 3.2f.dp
  private var bezierAngle = (PI / 6).toFloat()
  
  // Animated value from 0 to 1
  private var animatedFraction = 0f
  private var animatedFractionHolder = PropertyValuesHolder.ofFloat("animatedFractionHolder",
    0f) // Put 0 as a stub
  
  // Public properties
  var onColorChanged: (Int) -> Unit = {}
  var currentColor = 0
    private set
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    swapperRect.set(
      w / 2f - swapperBitmap.width / 2, h.f - swapperBitmap.height,
      w / 2f + swapperBitmap.width / 2, h.f
    )
    gradientRect.set(
      paddingLeft.f,
      paddingTop.f + circleStrokeWidth * 2,
      w.f - paddingEnd,
      h.f - paddingBottom - circleStrokeWidth * 2 - swapperRect.height() * 1.2f
    )
    initRainbowShader()
    initBlackAndWhiteShader()
    gradientPaint.shader = rainbowGradient
    rectRadius = w / 2f
    startAnimX = w / 2f
    endAnimX = -w * 2.5f
    radiusFloating = w * 1.3f
    radiusSelected = width / 2f - circleStrokeWidth / 2
    currentAnimX = startAnimX
    currentAnimRadius = radiusSelected
    currentY = h / 2f
    currentCircle.set(w / 2f, currentY, radiusSelected)
    drawGradientBitmap()
    currentColor = gradientBitmap.getPixel(gradientRect.width().i / 2, h / 2)
    circlePaint.color = currentColor
    bezierSpotStart = currentCircle.x + currentCircle.radius
    bezierSpotEnd = -w / 1.6f
    bezierSpotValue = bezierSpotStart
    bezierShape.init(bezierAngle, bezierVerticalOffset, bezierHorizontalOffset)
  }
  
  override fun onDraw(canvas: Canvas) {
    with(canvas) {
      execute {
        rotate(currentSwapperRotation, swapperRect.centerX(), swapperRect.centerY())
        drawBitmap(swapperBitmap, null, swapperRect, swapperPaint)
        drawBitmap(swapperBitmapStroke, null, swapperRect, swapperPaintStroke)
      }
      execute {
        canvas.scale(currentGradientScale, currentGradientScale, gradientRect.centerX(),
          gradientRect.centerY())
        execute {
          translate(gradientRect.left, gradientRect.top)
          drawPath(gradientPath, gradientStrokePaint)
          drawPath(gradientPath, STROKE_PAINT)
        }
        drawBitmap(gradientBitmap, gradientRect.left, gradientRect.top, gradientPaint)
        bezierShape.draw(canvas, currentCircle, bezierSpotValue,
          bezierSpotOffset * animatedFraction)
        currentCircle.draw(canvas, circleStrokePaint)
        val strokePaint = if (currentColor.isWhiterThan(0xDD)) STROKE_PAINT else STROKE_PAINT_LIGHT
        if (currentCircle.radius == radiusSelected) {
          currentCircle.drawStroke(canvas, circleStrokePaint.strokeWidth, strokePaint)
        }
        currentCircle.draw(canvas, circlePaint)
        currentCircle.drawInnerStroke(canvas, STROKE_PAINT_LIGHT)
      }
    }
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        if (event.y <= height - swapperBitmap.height) {
          isTouchInPalette = true
          setupValues(event.y)
          updateCircleAnimation()
        }
        return true
      }
      ACTION_MOVE -> {
        if (isTouchInPalette) {
          setupValues(event.y)
          invalidate()
          return true
        }
      }
      ACTION_UP, ACTION_CANCEL -> {
        if (isTouchInPalette) {
          setupValues(event.y)
          updateCircleAnimation(true)
        } else {
          startBouncyEffect()
        }
        isTouchInPalette = false
        return true
      }
    }
    return false
  }
  
  private fun startBouncyEffect() {
    with(bouncyAnimator) {
      cancel()
      addBouncyBackEffect(currentGradientScale, coefficient = 0.25f, inTheMiddle = {
        changePaletteColors()
      })
      addUpdateListener {
        currentGradientScale = animatedValue as Float
        invalidate()
      }
      duration = SWAP_ANIMATION_DURATION
      start()
    }
    with(swapperAnimator) {
      cancel()
      setFloatValues(currentSwapperRotation, 180f)
      addUpdateListener {
        currentSwapperRotation = animatedValue as Float
        invalidate()
      }
      interpolator = DecelerateInterpolator()
      doOnEnd { currentSwapperRotation = 0f }
      duration = SWAP_ANIMATION_DURATION
      start()
    }
  }
  
  private fun changePaletteColors() {
    if (swapperMode == SwapperMode.RAINBOW) {
      gradientPaint.shader = blackAndWhiteGradient
    } else {
      gradientPaint.shader = rainbowGradient
    }
    swapperMode = swapperMode.swap()
    drawGradientBitmap()
    currentColor = gradientBitmap.getPixel(gradientRect.width().i / 2,
      (currentY - gradientRect.top).i)
    onColorChanged(currentColor)
    circlePaint.color = currentColor
    invalidate()
  }
  
  private fun setupValues(y: Float) {
    currentY = y.coerceIn(gradientRect.top + GRADIENT_SENSITIVITY,
      gradientRect.bottom - GRADIENT_SENSITIVITY)
    currentCircle.y = currentY
    currentColor = gradientBitmap.getPixel(gradientRect.width().i / 2,
      (currentY - gradientRect.top).i)
    onColorChanged(currentColor)
    circlePaint.color = currentColor
  }
  
  private fun updateCircleAnimation(animateBack: Boolean = false) {
    currentCircle.set(width / 2f, currentY, radiusSelected)
    circleAnimator.cancel()
    if (animateBack) {
      xHolder.setFloatValues(currentAnimX, startAnimX)
      radiusHolder.setFloatValues(currentAnimRadius, radiusSelected)
      bezierHolder.setFloatValues(bezierSpotEnd, bezierSpotStart)
      animatedFractionHolder.setFloatValues(1f, 0f)
    } else {
      xHolder.setFloatValues(currentAnimX, endAnimX)
      radiusHolder.setFloatValues(currentAnimRadius, radiusFloating)
      bezierHolder.setFloatValues(bezierSpotStart, bezierSpotEnd)
      animatedFractionHolder.setFloatValues(0f, 1f)
    }
    circleAnimator.setValues(xHolder, radiusHolder, bezierHolder, animatedFractionHolder)
    kickInCircleAnimator()
  }
  
  private fun kickInCircleAnimator() {
    circleAnimator.apply {
      cancel()
      addUpdateListener {
        this@GradientPalette.animatedFraction = animatedFraction
        currentAnimX = getAnimatedValue("xHolder") as Float
        currentAnimRadius = getAnimatedValue("radiusHolder") as Float
        bezierSpotValue = getAnimatedValue("bezierHolder") as Float
        this@GradientPalette.animatedFraction = getAnimatedValue("animatedFractionHolder") as Float
        currentCircle.set(currentAnimX, currentY, currentAnimRadius)
        invalidate()
      }
      interpolator = AccelerateDecelerateInterpolator()
      duration = CIRCLE_ANIMATION_DURATION
      start()
    }
  }
  
  private fun initRainbowShader() {
    val colorsMap = mapOf(
      "#FFBFBF".c to 0.01f,
      "#FF0000".c to 0.09f,
      "#FFFF00".c to 0.16f,
      "#FFFF00".c to 0.21f,
      "#84FF00".c to 0.27f,
      "#0BE024".c to 0.34f,
      "#00FFFB".c to 0.44f,
      "#00FFFB".c to 0.47f,
      "#0000FF".c to 0.59f,
      "#FB00FF".c to 0.67f,
      "#FB00FF".c to 0.78f,
      "#460A57".c to 0.91f
    )
    rainbowGradient = LinearGradient(
      gradientRect.width() / 2, 0f,
      gradientRect.width() / 2, gradientRect.bottom,
      colorsMap.keys.toTypedArray().toIntArray(),
      colorsMap.values.toTypedArray().toFloatArray(),
      Shader.TileMode.CLAMP
    )
  }
  
  private fun initBlackAndWhiteShader() {
    val colors = intArrayOf("#FFFFFF".c, "#000000".c)
    val positions = floatArrayOf(0f, 0.95f)
    blackAndWhiteGradient = LinearGradient(
      gradientRect.width() / 2, gradientRect.top,
      gradientRect.width() / 2, gradientRect.bottom,
      colors, positions, Shader.TileMode.CLAMP
    )
  }
  
  private fun drawGradientBitmap() {
    gradientBitmap = Bitmap.createBitmap(gradientRect.width().i, gradientRect.height().i,
      Bitmap.Config.ARGB_8888)
    val canvas = Canvas(gradientBitmap)
    gradientPath.addRoundRect(0f, 0f, gradientRect.width(), gradientRect.height(),
      gradientRect.width() / 2, gradientRect.width() / 2, Path.Direction.CW)
    gradientRegion.setPath(gradientPath, Region(gradientRect.toRect()))
    canvas.drawPath(gradientPath, gradientPaint)
  }
  
  private enum class SwapperMode {
    RAINBOW,
    BLACK_AND_WHITE;
    
    fun swap(): SwapperMode {
      return if (this == RAINBOW) BLACK_AND_WHITE else RAINBOW
    }
  }
  
  class BezierShape {
    
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
    private val bezierPath = Path()
    private var angleRadians = 0f
    private var verticalOffset = 0f
    private var horizontalOffset = 0f
    
    fun init(angle: Float, verticalOffset: Float, horizontalOffset: Float) {
      angleRadians = angle
      this.verticalOffset = verticalOffset
      this.horizontalOffset = horizontalOffset
    }
    
    fun draw(canvas: Canvas, circle: Circle, triangleEnd: Float, bezierOffset: Float) {
      val x1 = (cos(angleRadians) * circle.radius + circle.x) - horizontalOffset
      val y1 = (sin(angleRadians) * circle.radius + circle.y) + verticalOffset
      
      val x3 = (cos(angleRadians) * circle.radius + circle.x) - horizontalOffset
      val y3 = (-sin(angleRadians) * circle.radius + circle.y) - verticalOffset
      
      with(bezierPath) {
        moveTo(x1, y1)
        cubicTo(
          triangleEnd + bezierOffset,
          circle.y - bezierOffset,
          triangleEnd + bezierOffset, circle.y + bezierOffset,
          x3, y3)
        close()
      }
      
      canvas.drawPath(bezierPath, bgPaint)
      bezierPath.reset()
    }
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
    
    fun drawInnerStroke(canvas: Canvas, strokePaint: Paint) {
      canvas.drawCircle(x, y, radius, strokePaint)
    }
    
    fun drawStroke(canvas: Canvas, strokePaintWidth: Float, paint: Paint) {
      canvas.drawCircle(x, y, radius + strokePaintWidth / 2, paint)
    }
  }
  
  private val String.c get() = Color.parseColor(this)
  
  private fun RectF.toRect(): Rect {
    return Rect(left.i, top.i, right.i, bottom.i)
  }
}
