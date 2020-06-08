package com.arsvechkarev.letta.views.gradientpalette

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import com.arsvechkarev.letta.core.model.Circle
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.extensions.i
import com.arsvechkarev.letta.extensions.toRect

class VerticalPalette : Palette {
  
  private var width = 0
  private var height = 0
  
  override fun initHolder(
    width: Int,
    height: Int,
    swapper: Drawable,
    swapperStroke: Drawable,
    padding: Padding
  ): ValuesHolder {
    this.width = width
    this.height = height
    val circleStrokeWidth = width / CIRCLE_STROKE_WIDTH
    val swapperBounds = Rect(
      width / 2 - swapper.intrinsicWidth / 2, height - swapper.intrinsicHeight,
      width / 2 + swapper.intrinsicWidth / 2, height
    )
    val swapperStrokeBounds = Rect(
      width / 2 - swapperStroke.intrinsicWidth / 2, height - swapperStroke.intrinsicHeight,
      width / 2 + swapperStroke.intrinsicWidth / 2, height
    )
    val gradientRectBounds = RectF(
      padding.left,
      padding.top + circleStrokeWidth,
      width.f - padding.right,
      height.f - padding.bottom - circleStrokeWidth - swapperBounds.height() * GRADIENT_RECT_BOTTOM_COEFF
    )
    val roundRectRadius = width / 2f
    val startAnimAxis = width / 2f
    val endAnimAxis = -width * END_ANIM_AXIS_COEFF
    val radiusFloating = width * RADIUS_FLOATING_COEFF
    val radiusSelected = width / 2f - circleStrokeWidth / 2
    val currentAxisValue = height / 2f
    val bezierSpotStart = width / 2f + radiusSelected
    val bezierSpotEnd = -width / BEZIER_SPOT_END_COEFF
    val gradientOuterStrokeWidth = width / GRADIENT_OUTER_STROKE_WIDTH_COEFF
    val gradientStrokeWidth = gradientOuterStrokeWidth * GRADIENT_STROKE_WIDTH_COEFF
    return ValuesHolder(
      swapperBounds = swapperBounds,
      swapperStrokeBounds = swapperStrokeBounds,
      gradientRectBounds = gradientRectBounds,
      roundRectRadius = roundRectRadius,
      startAnimAxis = startAnimAxis,
      endAnimAxis = endAnimAxis,
      radiusFloating = radiusFloating,
      radiusSelected = radiusSelected,
      currentAnimAxis = startAnimAxis,
      currentAnimRadius = radiusSelected,
      currentAxisValue = currentAxisValue,
      bezierSpotStart = bezierSpotStart,
      bezierSpotEnd = bezierSpotEnd,
      gradientOuterStrokeWidth = gradientOuterStrokeWidth,
      gradientStrokeWidth = gradientStrokeWidth,
      circleStrokeWidth = circleStrokeWidth
    )
  }
  
  override fun drawGradientRect(canvas: Canvas, gradientRect: RectF, gradientOuterPaint: Paint) {
    canvas.drawRoundRect(0f, 0f, gradientRect.width(), gradientRect.height(),
      gradientRect.width() / 2, gradientRect.width() / 2, gradientOuterPaint)
  }
  
  override fun getCircleX(circle: Circle) = circle.x
  
  override fun getCircleY(circle: Circle) = circle.y
  
  override fun isNotInSwapper(event: MotionEvent, swapper: Drawable) =
      event.y <= height - swapper.bounds.height()
  
  override fun updateAxisValue(event: MotionEvent, circle: Circle, gradientRect: RectF, gradientSensitivity: Int): Float {
    val value = event.y.coerceIn(gradientRect.top + gradientSensitivity,
      gradientRect.bottom - gradientSensitivity)
    circle.y = value
    return value
  }
  
  override fun getColorFromBitmap(gradientBitmap: Bitmap, gradientRect: RectF, currentAxisValue: Float) =
      gradientBitmap.getPixel(gradientRect.width().i / 2,
        (currentAxisValue - gradientRect.top).i)
  
  override fun updateCircle(
    currentCircle: Circle,
    currentAxisValue: Float,
    currentAnimAxis: Float,
    radius: Float
  ) {
    currentCircle.set(currentAnimAxis, currentAxisValue, radius)
  }
  
  override fun createBlackAndWhiteGradient(
    gradientRect: RectF,
    colors: IntArray,
    positions: FloatArray
  ) = LinearGradient(
    gradientRect.width() / 2, 0f,
    gradientRect.width() / 2, gradientRect.height(),
    colors, positions, Shader.TileMode.CLAMP
  )
  
  override fun createRainbowGradient(gradientRect: RectF, colors: IntArray, positions: FloatArray) =
      LinearGradient(
        gradientRect.width() / 2, 0f,
        gradientRect.width() / 2, gradientRect.height(),
        colors, positions, Shader.TileMode.CLAMP
      )
  
  override fun drawGradientPath(
    canvas: Canvas,
    gradientRect: RectF,
    gradientPath: Path,
    gradientPaint: Paint,
    gradientRegion: Region
  ) {
    gradientPath.addRoundRect(0f, 0f, gradientRect.width(), gradientRect.height(),
      gradientRect.width() / 2, gradientRect.width() / 2, Path.Direction.CW)
    gradientRegion.setPath(gradientPath, Region(gradientRect.toRect()))
    canvas.drawPath(gradientPath, gradientPaint)
  }
  
  override fun drawBezierShape(
    bezierShape: BezierShape,
    canvas: Canvas,
    circle: Circle,
    bezierDistance: Float,
    bezierOffset: Float
  ) {
    bezierShape.drawVertical(canvas, circle, bezierDistance, bezierOffset)
  }
}