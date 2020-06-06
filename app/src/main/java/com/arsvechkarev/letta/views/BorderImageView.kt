package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.COLOR_BORDER_LIGHT
import com.arsvechkarev.letta.extensions.getDimen

class BorderImageView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
  
  private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    color = COLOR_BORDER_LIGHT
    strokeWidth = context.getDimen(R.dimen.border_width)
  }
  private val borderRect = Rect()
  
  fun updateColor(color: Int) {
    setImageDrawable(null)
    setBackgroundColor(color)
  }
  
  fun updateDrawable(@DrawableRes resId: Int) {
    setBackgroundColor(Color.TRANSPARENT)
    setImageResource(resId)
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    borderRect.set(0, 0, w, h)
  }
  
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawRect(borderRect, borderPaint)
  }
}