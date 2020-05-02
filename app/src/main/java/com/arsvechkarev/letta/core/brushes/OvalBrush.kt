package com.arsvechkarev.letta.core.brushes

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import com.arsvechkarev.letta.R

class OvalBrush(
  resources: Resources, color: Int, brushSize: Float
) : BitmapBrush(color, brushSize, brushSize / THRESHOLD_COEFFICIENT) {
  
  private val rect = RectF()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
  }
  private val oval = BitmapFactory.decodeResource(resources, R.drawable.brush_oval)
  private val widthToHeightRatio = oval.width.toFloat() / oval.height.toFloat()
  
  override val type = BrushType.OVAL
  
  override fun onBrushSizeChanged(brushSize: Float) {
    thresholdPx = brushSize / THRESHOLD_COEFFICIENT
  }
  
  override fun draw(canvas: Canvas) {
    var i = 0
    while (i < points.size) {
      val x = points[i]
      val y = points[i + 1]
      val width = widthToHeightRatio * brushSize
      rect.set(x - width / 2, y - brushSize / 2, x + width / 2, y + brushSize / 2)
      canvas.drawBitmap(oval, null, rect, paint)
      i += 2
    }
  }
  
  override fun onExampleDraw(canvas: Canvas, x: Float, y: Float, brushSize: Float) {
    val width = widthToHeightRatio * brushSize
    rect.set(x - width / 2, y - brushSize / 2, x + width / 2, y + brushSize / 2)
    canvas.drawBitmap(oval, null, rect, paint)
  }
  
  companion object {
    private const val THRESHOLD_COEFFICIENT = 3f
  }
}