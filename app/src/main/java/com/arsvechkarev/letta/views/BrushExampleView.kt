package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.opengldrawing.Brush

class BrushExampleView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private var bitmap: Bitmap? = null
  private val bitmapRect = RectF()
  private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
  }
  
  private var cornersRadius = 0f
  private val selectionRect = RectF()
  private val selectionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Colors.GraySelected
  }
  
  fun updateBitmap(brush: Brush) {
    this.bitmap = BitmapFactory.decodeResource(resources, brush.stamp)
    invalidate()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val size = minOf(w, h)
    val offset = size / 10f
    cornersRadius = size / 12f
    selectionRect.set(0f, 0f, w.f, h.f)
    bitmapRect.set(offset, offset, w - offset, h - offset)
  }
  
  override fun onDraw(canvas: Canvas) {
    if (isSelected) {
      canvas.drawRoundRect(selectionRect, cornersRadius, cornersRadius, selectionPaint)
    }
    val bitmap = bitmap ?: return
    canvas.drawBitmap(bitmap, null, bitmapRect, bitmapPaint)
  }
}