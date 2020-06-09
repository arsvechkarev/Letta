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
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.opengldrawing.brushes.Brush

class BrushExampleView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private var bitmap: Bitmap? = null
  private val rect = RectF()
  private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
  }
  
  private val selectionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = 0x66FF0000
  }
  
  fun updateBitmap(brush: Brush) {
    this.bitmap = BitmapFactory.decodeResource(resources, brush.stamp)
    invalidate()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    rect.set(0f, 0f, w.f, h.f)
  }
  
  override fun onDraw(canvas: Canvas) {
    if (isSelected) {
      canvas.drawRect(rect, selectionPaint)
    }
    val bitmap = bitmap ?: return
    canvas.drawBitmap(bitmap, null, rect, bitmapPaint)
  }
}