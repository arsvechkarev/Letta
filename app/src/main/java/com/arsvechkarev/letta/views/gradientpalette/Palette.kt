package com.arsvechkarev.letta.views.gradientpalette

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.graphics.drawable.Drawable
import android.view.MotionEvent

interface Palette {
  fun initHolder(width: Int, height: Int, swapper: Drawable, swapperStroke: Drawable, padding: Padding, circleStrokeWidth: Float): ValuesHolder
  fun drawGradientRect(canvas: Canvas, gradientRect: RectF, gradientOuterPaint: Paint)
  fun drawBezierShape(bezierShape: BezierShape, canvas: Canvas, circle: Circle, bezierSpotValue: Float, bezierOffset: Float, circleX: Float, circleY: Float)
  fun getCircleX(circle: Circle): Float
  fun getCircleY(circle: Circle): Float
  fun drawCircle(circle: Circle, canvas: Canvas, circleStrokePaint: Paint)
  fun drawCircleStroke(circle: Circle, canvas: Canvas, strokeWidth: Float, strokePaint: Paint)
  fun drawCircleInnerStroke(circle: Circle, canvas: Canvas, circlePaint: Paint)
  fun isNotInSwapper(event: MotionEvent, swapper: Drawable): Boolean
  fun getActiveAxis(event: MotionEvent): Float
  fun getCoercedCurrentAxisValue(axisValue: Float, gradientRect: RectF, gradientSensitivity: Int): Float
  fun updateCircleAxis(circle: Circle, axisValue: Float)
  fun getColorFromBitmap(gradientBitmap: Bitmap, gradientRect: RectF, currentAxisValue: Float): Int
  fun updateCircleAnimation(currentCircle: Circle, currentAxisValue: Float, radiusSelected: Float)
  fun createBlackAndWhiteGradient(gradientRect: RectF, colors: IntArray, positions: FloatArray): LinearGradient
  fun createRainbowGradient(gradientRect: RectF, colors: Map<Int, Float>): LinearGradient
  fun drawGradientPath(canvas: Canvas, gradientRect: RectF, gradientPath: Path, gradientPaint: Paint, gradientRegion: Region)
}