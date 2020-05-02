package com.arsvechkarev.letta.views.gradientpalette

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
import android.graphics.RectF
import android.graphics.Region
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
import com.arsvechkarev.letta.animations.doOnEnd
import com.arsvechkarev.letta.utils.LIGHT_GRAY
import com.arsvechkarev.letta.utils.STROKE_PAINT
import com.arsvechkarev.letta.utils.c
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.drawBounds
import com.arsvechkarev.letta.utils.execute
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.i
import kotlin.math.PI

/**
 * Gradient palette for choosing colors
 *
 * Note: `axis` means value alongside the longest axis (X if orientation is vertical, Y otherwise)
 *
 * @see Palette
 * @see VerticalPalette
 * @see HorizontalPalette
 */
class GradientPalette @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val palette: Palette
  
  private val gradientOuterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = LIGHT_GRAY
    style = Paint.Style.STROKE
    strokeWidth = (8.2f).dp
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
  private var touchInPalette = false
  private var gradientScale = 1f
  
  // Circle and animation
  private val circleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
    this.strokeWidth = circleStrokeWidth
    style = Paint.Style.STROKE
  }
  
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var rectRadius = 0f
  private var circle = Circle()
  private var radiusSelected = 0f
  private var radiusFloating = 0f
  private var startAnimAxis = 0f
  private var endAnimAxis = 0f
  private var currentAxisValue = 0f // Value of Y if orientation is vertical, X otherwise
  private var currentAnimAxis = 0f
  private var currentAnimRadius = 0f
  private val circleAnimator = ValueAnimator()
  private val axisHolder = PropertyValuesHolder.ofFloat("axis", 0f) // Put 0 as a stub
  private val radiusHolder = PropertyValuesHolder.ofFloat("radius", 0f) // Put 0 as a stub
  
  // Swapper
  private var swapperMode = SwapperMode.RAINBOW
  private var currentSwapperRotation = 0f
  private val swapper = ContextCompat.getDrawable(context, R.drawable.ic_swap)!!.apply {
    colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
  }
  private val swapperStroke = ContextCompat.getDrawable(context, R.drawable.ic_swap_stroke)!!
      .apply { colorFilter = PorterDuffColorFilter(LIGHT_GRAY, PorterDuff.Mode.SRC_ATOP) }
  private val swapperAnimator = ValueAnimator()
  private val bouncyAnimator = ValueAnimator()
  
  // Bezier path
  private val bezierShape = BezierShape()
  private var bezierSpotStart = 0f
  private var bezierSpotEnd = 0f
  private var bezierSpotValue = 0f
  private val bezierHolder = PropertyValuesHolder.ofFloat("bezier", 0f) // Put 0 as a stub
  private var bezierSpotOffset = 22.dp
  private var bezierVerticalOffset = 10.dp
  private var bezierHorizontalOffset = 3.2f.dp
  private var bezierAngle = (PI / 6).toFloat()
  
  private var animatedFraction = 0f
  private val animatedFractionHolder = PropertyValuesHolder.ofFloat("fraction",
    0f) // Put 0 as a stub
  
  var onColorChanged: (Int) -> Unit = {}
  var currentColor = 0
    private set
  
  init {
    val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.GradientPalette,
      defStyleAttr, 0)
    palette = when (attributes.getInt(R.styleable.GradientPalette_android_orientation, 0)) {
      0 -> HorizontalPalette()
      1 -> VerticalPalette()
      else -> throw IllegalStateException("Unknown default state")
    }
    attributes.recycle()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val padding = Padding(paddingLeft.f, paddingTop.f, paddingRight.f, paddingBottom.f)
    val holder = palette.initHolder(w, h, swapper, swapperStroke, padding, circleStrokeWidth)
    swapper.bounds = holder.swapperBounds
    swapperStroke.bounds = holder.swapperStrokeBounds
    gradientRect.set(holder.gradientRectBounds)
    initRainbowShader()
    initBlackAndWhiteShader()
    gradientPaint.shader = rainbowGradient
    rectRadius = holder.roundRectRadius
    startAnimAxis = holder.startAnimAxis
    endAnimAxis = holder.endAnimAxis
    radiusFloating = holder.radiusFloating
    radiusSelected = holder.radiusSelected
    currentAnimAxis = holder.currentAnimAxis
    currentAnimRadius = holder.currentAnimRadius
    currentAxisValue = holder.currentAxisValue
    circle.set(holder.minSizeHalf, currentAxisValue, radiusSelected)
    drawGradientBitmap()
    currentColor = palette.getColorFromBitmap(gradientBitmap, gradientRect, currentAxisValue)
    circlePaint.color = currentColor
    bezierSpotStart = holder.bezierSpotStart
    bezierSpotEnd = holder.bezierSpotEnd
    bezierSpotValue = bezierSpotStart
    bezierShape.init(bezierAngle, bezierVerticalOffset, bezierHorizontalOffset)
  }
  
  override fun onDraw(canvas: Canvas) {
    with(canvas) {
      execute {
        rotate(currentSwapperRotation, swapper.bounds.exactCenterX(), swapper.bounds.exactCenterY())
        swapper.draw(canvas)
        swapperStroke.draw(canvas)
      }
      execute {
        canvas.scale(gradientScale, gradientScale, gradientRect.centerX(), gradientRect.centerY())
        execute {
          translate(gradientRect.left, gradientRect.top)
          palette.drawGradientRect(canvas, gradientRect, gradientOuterPaint)
          drawPath(gradientPath, gradientStrokePaint)
        }
        drawBitmap(gradientBitmap, gradientRect.left, gradientRect.top, gradientPaint)
        val bezierOffset = bezierSpotOffset * animatedFraction
        println("spotValue = $bezierSpotValue")
        bezierShape.draw(canvas, circle, bezierSpotValue, bezierOffset, palette.getCircleX(circle),
          palette.getCircleY(circle))
        palette.drawCircle(circle, canvas, circleStrokePaint)
        if (circle.radius == radiusSelected) {
          palette.drawCircleStroke(circle, canvas, circleStrokePaint.strokeWidth,
            STROKE_PAINT)
        }
        palette.drawCircle(circle, canvas, circlePaint)
        palette.drawCircleInnerStroke(circle, canvas, STROKE_PAINT)
      }
      drawBounds(canvas)
    }
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val axis = palette.getActiveAxis(event)
    when (event.action) {
      ACTION_DOWN -> {
        if (palette.isNotInSwapper(event, swapper)) {
          touchInPalette = true
          updateValues(axis)
          updateCircleAnimation(animateBack = false)
        }
        return true
      }
      ACTION_MOVE -> {
        if (touchInPalette) {
          updateValues(axis)
          invalidate()
          return true
        }
      }
      ACTION_UP, ACTION_CANCEL -> {
        if (touchInPalette) {
          updateValues(axis)
          updateCircleAnimation(animateBack = true)
        } else {
          if (gradientScale == 1f) { // If scale is 1 => gradient isn't being animated now
            startBouncyEffect()
          }
        }
        touchInPalette = false
        return true
      }
    }
    return false
  }
  
  private fun startBouncyEffect() {
    with(bouncyAnimator) {
      cancel()
      addBouncyBackEffect(gradientScale, coefficient = 0.25f, inTheMiddle = {
        changePaletteColors()
      })
      addUpdateListener {
        gradientScale = animatedValue as Float
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
    currentColor = palette.getColorFromBitmap(gradientBitmap, gradientRect, currentAxisValue)
    onColorChanged(currentColor)
    circlePaint.color = currentColor
    invalidate()
  }
  
  private fun updateValues(axisValue: Float) {
    currentAxisValue = palette.getCoercedCurrentAxisValue(axisValue, gradientRect,
      GRADIENT_SENSITIVITY)
    palette.updateCircleAxis(circle, currentAxisValue)
    currentColor = palette.getColorFromBitmap(gradientBitmap, gradientRect, currentAxisValue)
    onColorChanged(currentColor)
    circlePaint.color = currentColor
  }
  
  private fun updateCircleAnimation(animateBack: Boolean) {
    palette.updateCircleAnimation(circle, currentAxisValue, radiusSelected)
    if (animateBack) {
      axisHolder.setFloatValues(currentAnimAxis, startAnimAxis)
      radiusHolder.setFloatValues(currentAnimRadius, radiusSelected)
      bezierHolder.setFloatValues(bezierSpotValue, bezierSpotStart)
      animatedFractionHolder.setFloatValues(animatedFraction, 0f)
    } else {
      axisHolder.setFloatValues(currentAnimAxis, endAnimAxis)
      radiusHolder.setFloatValues(currentAnimRadius, radiusFloating)
      bezierHolder.setFloatValues(bezierSpotValue, bezierSpotEnd)
      animatedFractionHolder.setFloatValues(animatedFraction, 1f)
    }
    kickInCircleAnimator()
  }
  
  private fun kickInCircleAnimator() {
    circleAnimator.apply {
      cancel()
      setValues(axisHolder, radiusHolder, bezierHolder, animatedFractionHolder)
      addUpdateListener {
        this@GradientPalette.animatedFraction = getAnimatedValue("fraction") as Float
        currentAnimAxis = getAnimatedValue("axis") as Float
        currentAnimRadius = getAnimatedValue("radius") as Float
        bezierSpotValue = getAnimatedValue("bezier") as Float
        circle.set(currentAnimAxis, currentAxisValue, currentAnimRadius)
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
    rainbowGradient = palette.createRainbowGradient(gradientRect, colorsMap)
  }
  
  private fun initBlackAndWhiteShader() {
    val colors = intArrayOf("#FFFFFF".c, "#000000".c)
    val positions = floatArrayOf(0f, 0.95f)
    blackAndWhiteGradient = palette.createBlackAndWhiteGradient(gradientRect, colors, positions)
  }
  
  private fun drawGradientBitmap() {
    gradientBitmap = Bitmap.createBitmap(gradientRect.width().i, gradientRect.height().i,
      Bitmap.Config.ARGB_8888)
    val canvas = Canvas(gradientBitmap)
    palette.drawGradientPath(canvas, gradientRect, gradientPath, gradientPaint, gradientRegion)
  }
  
  private enum class SwapperMode {
    RAINBOW,
    BLACK_AND_WHITE;
    
    fun swap(): SwapperMode {
      return if (this == RAINBOW) BLACK_AND_WHITE else RAINBOW
    }
  }
  
  companion object {
    private val circleStrokeWidth = 7.dp
    private const val GRADIENT_SENSITIVITY = 2
    private const val CIRCLE_ANIMATION_DURATION = 150L
    private const val SWAP_ANIMATION_DURATION = 200L
  }
}
