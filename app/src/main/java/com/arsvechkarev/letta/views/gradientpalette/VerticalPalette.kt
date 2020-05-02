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
import com.arsvechkarev.letta.utils.STROKE_PAINT
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.i
import com.arsvechkarev.letta.utils.toRect

class VerticalPalette : Palette {
  
  private var width = 0
  private var height = 0
  
  override fun initHolder(
    width: Int,
    height: Int,
    swapper: Drawable,
    swapperStroke: Drawable,
    padding: Padding,
    circleStrokeWidth: Float
  ): ValuesHolder {
    this.width = width
    this.height = height
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
      height.f - padding.bottom - circleStrokeWidth - swapperBounds.height() * 1.1f
    )
    val rectRadius = width / 2f
    val startAnimAxis = width / 2f
    val endAnimAxis = -width * 2.5f
    val radiusFloating = width * 1.3f
    val radiusSelected = width / 2f - circleStrokeWidth / 2
    val currentAxisValue = height / 2f
    val bezierSpotStart = width / 2f + radiusSelected
    val bezierSpotEnd = -width / 1.6f
    return ValuesHolder(
      swapperBounds = swapperBounds,
      swapperStrokeBounds = swapperStrokeBounds,
      gradientRectBounds = gradientRectBounds,
      roundRectRadius = rectRadius,
      startAnimAxis = startAnimAxis,
      endAnimAxis = endAnimAxis,
      radiusFloating = radiusFloating,
      radiusSelected = radiusSelected,
      currentAnimAxis = startAnimAxis,
      currentAnimRadius = radiusSelected,
      currentAxisValue = currentAxisValue,
      minSizeHalf = width / 2f,
      bezierSpotStart = bezierSpotStart,
      bezierSpotEnd = bezierSpotEnd
    )
  }
  
  override fun drawGradientRect(canvas: Canvas, gradientRect: RectF, gradientOuterPaint: Paint) {
    canvas.drawRoundRect(0f, 0f, gradientRect.width(), gradientRect.height(),
      gradientRect.width() / 2, gradientRect.width() / 2, gradientOuterPaint)
  }
  
  override fun getCircleX(circle: Circle) = circle.x
  
  override fun getCircleY(circle: Circle) = circle.y
  
  override fun drawCircle(circle: Circle, canvas: Canvas, circleStrokePaint: Paint) {
    circle.draw(canvas, circleStrokePaint)
  }
  
  override fun drawCircleStroke(circle: Circle, canvas: Canvas, strokeWidth: Float, strokePaint: Paint) {
    circle.drawStroke(canvas, strokeWidth, STROKE_PAINT)
  }
  
  override fun drawCircleInnerStroke(circle: Circle, canvas: Canvas, circlePaint: Paint) {
    circle.drawInnerStroke(canvas, STROKE_PAINT)
  }
  
  override fun isNotInSwapper(event: MotionEvent, swapper: Drawable) =
      event.y <= height - swapper.bounds.height()
  
  override fun getActiveAxis(event: MotionEvent) = event.y
  
  override fun getCoercedCurrentAxisValue(axisValue: Float, gradientRect: RectF, gradientSensitivity: Int) =
      axisValue.coerceIn(gradientRect.top + gradientSensitivity,
        gradientRect.bottom - gradientSensitivity)
  
  override fun updateCircleAxis(circle: Circle, axisValue: Float) {
    circle.y = axisValue
  }
  
  override fun getColorFromBitmap(gradientBitmap: Bitmap, gradientRect: RectF, currentAxisValue: Float) =
      gradientBitmap.getPixel(gradientRect.width().i / 2,
        (currentAxisValue - gradientRect.top).i)
  
  override fun updateCircleAnimation(currentCircle: Circle, currentAxisValue: Float, radiusSelected: Float) {
    currentCircle.set(width / 2f, currentAxisValue, radiusSelected)
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
  
  override fun createRainbowGradient(gradientRect: RectF, colors: Map<Int, Float>) =
      LinearGradient(
        gradientRect.width() / 2, 0f,
        gradientRect.width() / 2, gradientRect.height(),
        colors.keys.toTypedArray().toIntArray(),
        colors.values.toTypedArray().toFloatArray(),
        Shader.TileMode.CLAMP
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
}