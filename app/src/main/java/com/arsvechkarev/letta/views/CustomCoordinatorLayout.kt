package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.TEMP_PAINT
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.extensions.statusBarHeight

class CustomCoordinatorLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {
  
  init {
    setPadding(0, context.statusBarHeight, 0, 0)
  }
  
  override fun dispatchDraw(canvas: Canvas) {
    super.dispatchDraw(canvas)
    TEMP_PAINT.color = Colors.Background
    canvas.drawRect(0f, 0f, canvas.width.f, context.statusBarHeight.f, TEMP_PAINT)
    StatusBarBackground.drawItself(context, canvas)
  }
}