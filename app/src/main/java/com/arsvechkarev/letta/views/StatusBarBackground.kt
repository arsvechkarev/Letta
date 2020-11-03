package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.extensions.TEMP_PAINT
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.extensions.statusBarHeight

class StatusBarBackground @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val statusBarHeight = context.statusBarHeight
    setMeasuredDimension(
      resolveSize(widthSize, widthMeasureSpec),
      resolveSize(statusBarHeight, heightMeasureSpec)
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawColor(Colors.StatusBar)
  }
  
  companion object {
  
    fun draw(context: Context, canvas: Canvas, color: Int) {
      TEMP_PAINT.color = color
      canvas.drawRect(0f, 0f, canvas.width.f, context.statusBarHeight.f, TEMP_PAINT)
    }
  }
}