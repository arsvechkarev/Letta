package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.letta.core.Colors
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      StatusBarBackground.draw(context, canvas, Colors.Background)
    } else {
      StatusBarBackground.draw(context, canvas, Color.BLACK)
    }
    StatusBarBackground.draw(context, canvas, Colors.StatusBar)
  }
}