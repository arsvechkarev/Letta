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
import com.arsvechkarev.letta.core.model.Circle

/**
 * Palette with gradient and animated circle
 *
 * @see VerticalPalette
 * @see HorizontalPalette
 */
interface Palette {
  
  fun initHolder(width: Int, height: Int, swapper: Drawable,
                 swapperStroke: Drawable, padding: Padding): ValuesHolder
  
  fun drawGradientRect(canvas: Canvas, gradientRect: RectF, gradientOuterPaint: Paint)
  
  fun getCircleX(circle: Circle): Float
  
  fun getCircleY(circle: Circle): Float
  
  fun isNotInSwapper(event: MotionEvent, swapper: Drawable): Boolean
  
  fun updateAxisValue(event: MotionEvent, circle: Circle, gradientRect: RectF, gradientSensitivity: Int): Float
  
  fun getColorFromBitmap(gradientBitmap: Bitmap, gradientRect: RectF, currentAxisValue: Float): Int
  
  fun updateCircle(currentCircle: Circle, currentAxisValue: Float, currentAnimAxis: Float, radius: Float)
  
  fun createBlackAndWhiteGradient(gradientRect: RectF, colors: IntArray,
                                  positions: FloatArray): LinearGradient
  
  fun createRainbowGradient(gradientRect: RectF, colors: IntArray, positions: FloatArray): LinearGradient
  
  fun drawGradientPath(canvas: Canvas, gradientRect: RectF, gradientPath: Path,
                       gradientPaint: Paint, gradientRegion: Region)
  
  fun drawBezierShape(bezierShape: BezierShape, canvas: Canvas, circle: Circle,
                      bezierDistance: Float, bezierOffset: Float)
}