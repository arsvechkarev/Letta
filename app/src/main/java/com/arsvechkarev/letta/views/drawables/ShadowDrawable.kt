package com.arsvechkarev.letta.views.drawables

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.extensions.f

class ShadowDrawable : Drawable() {
  
  private val paint = Paint(ANTI_ALIAS_FLAG)
  
  override fun onBoundsChange(bounds: Rect) {
    paint.shader = LinearGradient(
      bounds.width() / 2f, 0f,
      bounds.width() / 2f, bounds.height().f,
      intArrayOf(Colors.BorderVeryLight, Color.TRANSPARENT),
      null, Shader.TileMode.CLAMP
    )
  }
  
  override fun draw(canvas: Canvas) {
    canvas.drawPaint(paint)
  }
  
  override fun setAlpha(alpha: Int) {
    paint.alpha = alpha
  }
  
  override fun setColorFilter(colorFilter: ColorFilter?) {
    paint.colorFilter = colorFilter
  }
  
  override fun getOpacity() = PixelFormat.OPAQUE
}