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

class HorizontalPalette : Palette {
  
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
      width - swapper.intrinsicWidth, height / 2 - swapper.intrinsicHeight / 2,
      width, height / 2 + swapper.intrinsicHeight / 2
    )
    val swapperStrokeBounds = Rect(
      width - swapperStroke.intrinsicWidth, height / 2 - swapperStroke.intrinsicHeight / 2,
      width, height / 2 + swapperStroke.intrinsicHeight / 2
    )
    val gradientRectBounds = RectF(
      padding.left + circleStrokeWidth,
      padding.top,
      width.f - padding.right - circleStrokeWidth - swapperBounds.width() * 1.1f,
      height.f - padding.bottom
    )
    val roundRectRadius = height / 2f
    val startAnimAxis = height / 2f
    val endAnimAxis = -height * 2.5f
    val radiusFloating = height * 1.3f
    val radiusSelected = height / 2f - circleStrokeWidth / 2
    val currentAxisValue = width / 2f
    val bezierSpotStart = height / 2f + radiusSelected
    val bezierSpotEnd = -height / 1.6f
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
      bezierSpotEnd = bezierSpotEnd
    )
  }
  
  override fun drawGradientRect(canvas: Canvas, gradientRect: RectF, gradientOuterPaint: Paint) {
    canvas.drawRoundRect(0f, 0f, gradientRect.width(), gradientRect.height(),
      gradientRect.height() / 2, gradientRect.height() / 2, gradientOuterPaint)
  }
  
  override fun getCircleX(circle: Circle) = circle.y
  
  override fun getCircleY(circle: Circle) = circle.x
  
  override fun drawCircle(canvas: Canvas, radius: Float, x: Float, y: Float, circleStrokePaint: Paint) {
    canvas.drawCircle(x, y, radius, circleStrokePaint)
  }
  
  override fun drawCircleStroke(canvas: Canvas, radius: Float, x: Float, y: Float,
                                strokeWidth: Float, strokePaint: Paint) {
    canvas.drawCircle(x, y, radius + strokeWidth / 2, strokePaint)
  }
  
  override fun isNotInSwapper(event: MotionEvent, swapper: Drawable) =
      event.x <= width - swapper.bounds.width()
  
  override fun updateAxisValue(event: MotionEvent, circle: Circle,
                               gradientRect: RectF, gradientSensitivity: Int): Float {
    val value = event.x.coerceIn(gradientRect.left + gradientSensitivity,
      gradientRect.right - gradientSensitivity)
    circle.x = value
    return value
  }
  
  override fun getColorFromBitmap(gradientBitmap: Bitmap, gradientRect: RectF, currentAxisValue: Float) =
      gradientBitmap.getPixel((currentAxisValue - gradientRect.left).i,
        gradientRect.height().i / 2)
  
  override fun updateCircle(
    currentCircle: Circle,
    currentAxisValue: Float,
    currentAnimAxis: Float,
    radius: Float
  ) {
    currentCircle.set(currentAxisValue, currentAnimAxis, radius)
  }
  
  override fun createBlackAndWhiteGradient(
    gradientRect: RectF,
    colors: IntArray,
    positions: FloatArray
  ) = LinearGradient(
    0f, gradientRect.height() / 2,
    gradientRect.width(), gradientRect.height() / 2,
    colors, positions, Shader.TileMode.CLAMP
  )
  
  override fun createRainbowGradient(gradientRect: RectF, colors: Map<Int, Float>) =
      LinearGradient(
        0f, gradientRect.height() / 2,
        gradientRect.width(), gradientRect.height() / 2,
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
      gradientRect.height() / 2, gradientRect.height() / 2, Path.Direction.CW)
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
    bezierShape.drawHorizontal(canvas, circle, bezierDistance, bezierOffset)
  }
}