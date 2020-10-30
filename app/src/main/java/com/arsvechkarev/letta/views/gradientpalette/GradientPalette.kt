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
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import androidx.core.content.ContextCompat
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.STROKE_PAINT
import com.arsvechkarev.letta.core.animations.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.core.animations.addBouncyBackEffect
import com.arsvechkarev.letta.core.model.Circle
import com.arsvechkarev.letta.extensions.cancelIfRunning
import com.arsvechkarev.letta.extensions.execute
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.extensions.i
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette.GradientMode.BLACK_AND_WHITE
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette.GradientMode.RAINBOW
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette.GradientMode.RAINBOW_DARK

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
  
  private var gradientMode = RAINBOW
  private val gradientOuterStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Colors.BorderLight
    style = Paint.Style.STROKE
  }
  private val gradientStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
    style = Paint.Style.STROKE
  }
  private val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val gradientRect = RectF()
  private val gradientPath = Path()
  private val gradientRegion = Region()
  private lateinit var rainbowGradient: LinearGradient
  private lateinit var rainbowDarkGradient: LinearGradient
  private lateinit var blackAndWhiteGradient: LinearGradient
  private lateinit var gradientBitmap: Bitmap
  private var gradientScale = 1f
  
  // Circle
  private val circleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = ContextCompat.getColor(context, R.color.light_background)
    style = Paint.Style.STROKE
  }
  
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var rectRadius = 0f
  private var circle = Circle()
  private var radiusSelected = 0f
  private var radiusFloating = 0f
  private var startAnimAxis = 0f
  private var endAnimAxis = 0f
  private var currentMovingAxis = 0f // Value of Y if orientation is vertical, X otherwise
  private var currentAnimAxis = 0f
  private var currentAnimRadius = 0f
  private val circleAnimator = ValueAnimator()
  private val axisHolder = PropertyValuesHolder.ofFloat(AXIS, 0f) // Put 0 as a stub
  private val radiusHolder = PropertyValuesHolder.ofFloat(RADIUS, 0f) // Put 0 as a stub
  
  private val bouncyAnimator = ValueAnimator().apply {
    addUpdateListener {
      gradientScale = animatedValue as Float
      invalidate()
    }
    duration = SWAP_ANIMATION_DURATION
  }
  
  // Bezier path
  private val bezierShape = BezierShape()
  private var bezierSpotStart = 0f
  private var bezierSpotEnd = 0f
  private var bezierSpotValue = 0f
  private val bezierHolder = PropertyValuesHolder.ofFloat(BEZIER, 0f) // Put 0 as a stub
  
  private var animatedFraction = 0f
  private val animatedFractionHolder = PropertyValuesHolder.ofFloat(FRACTION, 0f) // Put 0 as a stub
  
  var onColorChanged: (Int) -> Unit = {}
  var onColorChangedByTouch: (Int) -> Unit = {}
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
    val holder = palette.initHolder(w, h, padding)
    gradientRect.set(holder.gradientRectBounds)
    initGradients()
    gradientPaint.shader = rainbowGradient
    rectRadius = holder.roundRectRadius
    startAnimAxis = holder.startAnimAxis
    endAnimAxis = holder.endAnimAxis
    radiusFloating = holder.radiusFloating
    radiusSelected = holder.radiusSelected
    currentAnimAxis = holder.currentAnimAxis
    currentAnimRadius = holder.currentAnimRadius
    currentMovingAxis = holder.currentAxisValue
    palette.updateCircle(circle, currentMovingAxis, currentAnimAxis, radiusSelected)
    drawGradientBitmap()
    currentColor = palette.getColorFromBitmap(gradientBitmap, gradientRect, currentMovingAxis)
    onColorChanged(currentColor)
    circlePaint.color = currentColor
    bezierSpotStart = holder.bezierSpotStart
    bezierSpotEnd = holder.bezierSpotEnd
    bezierSpotValue = bezierSpotStart
    gradientOuterStrokePaint.strokeWidth = holder.gradientOuterStrokeWidth
    gradientStrokePaint.strokeWidth = holder.gradientStrokeWidth
    circleStrokePaint.strokeWidth = holder.circleStrokeWidth
    bezierShape.init(BEZIER_ANGLE, BEZIER_VERTICAL_OFFSET, BEZIER_HORIZONTAL_OFFSET)
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.execute {
      canvas.scale(gradientScale, gradientScale, gradientRect.centerX(), gradientRect.centerY())
      canvas.execute {
        translate(gradientRect.left, gradientRect.top)
        palette.drawGradientRect(canvas, gradientRect, gradientOuterStrokePaint)
        drawPath(gradientPath, gradientStrokePaint)
      }
      drawBitmap(gradientBitmap, gradientRect.left, gradientRect.top, gradientPaint)
      val bezierOffset = BEZIER_SPOT_OFFSET * animatedFraction
      val bezierDistance = bezierSpotValue + bezierOffset
      palette.drawBezierShape(bezierShape, canvas, circle, bezierDistance, bezierOffset)
      canvas.drawCircle(circle.x, circle.y, circle.radius, circleStrokePaint)
      if (circle.radius == radiusSelected) {
        canvas.drawCircle(
          circle.x, circle.y, circle.radius + circleStrokePaint.strokeWidth / 2, STROKE_PAINT
        )
      }
      canvas.drawCircle(circle.x, circle.y, circle.radius, circlePaint)
      canvas.drawCircle(circle.x, circle.y, circle.radius, STROKE_PAINT)
    }
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        updateValues(event)
        updateCircleAnimation(animateBack = false)
        return true
      }
      ACTION_MOVE -> {
        updateValues(event)
        invalidate()
        return true
      }
      ACTION_UP, ACTION_CANCEL -> {
        updateValues(event)
        updateCircleAnimation(animateBack = true)
        return true
      }
    }
    return false
  }
  
  fun swapGradientMode() {
    bouncyAnimator.cancelIfRunning()
    bouncyAnimator.addBouncyBackEffect(inTheMiddle = { changePaletteColors() })
    bouncyAnimator.start()
  }
  
  private fun changePaletteColors() {
    gradientPaint.shader = when (gradientMode) {
      RAINBOW -> rainbowDarkGradient
      RAINBOW_DARK -> blackAndWhiteGradient
      BLACK_AND_WHITE -> rainbowGradient
    }
    gradientMode = gradientMode.swap()
    drawGradientBitmap()
    currentColor = palette.getColorFromBitmap(gradientBitmap, gradientRect, currentMovingAxis)
    onColorChanged(currentColor)
    onColorChangedByTouch(currentColor)
    circlePaint.color = currentColor
    invalidate()
  }
  
  private fun updateValues(event: MotionEvent) {
    currentMovingAxis = palette.updateAxisValue(event, circle, gradientRect, GRADIENT_SENSITIVITY)
    currentColor = palette.getColorFromBitmap(gradientBitmap, gradientRect, currentMovingAxis)
    onColorChanged(currentColor)
    onColorChangedByTouch(currentColor)
    circlePaint.color = currentColor
  }
  
  private fun updateCircleAnimation(animateBack: Boolean) {
    palette.updateCircle(circle, currentMovingAxis, currentAnimAxis, radiusSelected)
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
        this@GradientPalette.animatedFraction = getAnimatedValue(FRACTION) as Float
        currentAnimAxis = getAnimatedValue(AXIS) as Float
        currentAnimRadius = getAnimatedValue(RADIUS) as Float
        bezierSpotValue = getAnimatedValue(BEZIER) as Float
        palette.updateCircle(circle, currentMovingAxis, currentAnimAxis, currentAnimRadius)
        invalidate()
      }
      interpolator = AccelerateDecelerateInterpolator
      duration = CIRCLE_ANIMATION_DURATION
      start()
    }
  }
  
  private fun initGradients() {
    rainbowGradient = palette.createGradient(gradientRect, rainbowColors, rainbowPositions)
    rainbowDarkGradient = palette.createGradient(gradientRect, rainbowDarkColors, rainbowPositions)
    blackAndWhiteGradient = palette.createGradient(gradientRect, blackAndWhiteColors,
      blackAndWhitePositions)
  }
  
  private fun drawGradientBitmap() {
    gradientBitmap = Bitmap.createBitmap(gradientRect.width().i, gradientRect.height().i,
      Bitmap.Config.ARGB_8888)
    val canvas = Canvas(gradientBitmap)
    palette.drawGradientPath(canvas, gradientRect, gradientPath, gradientPaint, gradientRegion)
  }
  
  private enum class GradientMode {
    RAINBOW,
    RAINBOW_DARK,
    BLACK_AND_WHITE;
    
    fun swap(): GradientMode {
      return when (this) {
        RAINBOW -> RAINBOW_DARK
        RAINBOW_DARK -> BLACK_AND_WHITE
        BLACK_AND_WHITE -> RAINBOW
      }
    }
  }
  
  companion object {
    const val SWAP_ANIMATION_DURATION = 400L
  
    private const val GRADIENT_SENSITIVITY = 2
    private const val CIRCLE_ANIMATION_DURATION = 150L
  
    private const val FRACTION = "fraction"
    private const val AXIS = "axis"
    private const val RADIUS = "radius"
    private const val BEZIER = "bezier"
  
    private val rainbowColors = intArrayOf(
      0xffff7878.toInt(),
      0xffff0000.toInt(),
      0xffffe414.toInt(),
      0xfffffb00.toInt(),
      0xff51ff00.toInt(),
      0xff00ffa2.toInt(),
      0xff00eaff.toInt(),
      0xff2457ff.toInt(),
      0xff9d00ff.toInt(),
      0xffff00c3.toInt(),
      0xff7a1496.toInt()
    )
  
    private val rainbowDarkColors = intArrayOf(
      0xff944646.toInt(),
      0xff8a0000.toInt(),
      0xff8a7b0a.toInt(),
      0xff999700.toInt(),
      0xff45802a.toInt(),
      0xff018f5b.toInt(),
      0xff008a96.toInt(),
      0xff001d7d.toInt(),
      0xff590091.toInt(),
      0xff8f006d.toInt(),
      0xff350842.toInt()
    )
  
    private val rainbowPositions = floatArrayOf(
      0.00f,
      0.07f,
      0.20f,
      0.25f,
      0.37f,
      0.45f,
      0.53f,
      0.63f,
      0.76f,
      0.88f,
      0.99f
    )
  
    private val blackAndWhiteColors = intArrayOf(
      0xffffffff.toInt(),
      0xff000000.toInt()
    )
  
    private val blackAndWhitePositions = floatArrayOf(0f, 0.95f)
  }
}
