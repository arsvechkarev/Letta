package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import com.arsvechkarev.letta.utils.execute
import kotlin.math.min

class CenterHoleImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : Image(context, attrs, defStyleAttr) {
  
  private val path = Path()
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    path.toggleInverseFillType()
  }
  
  override fun onDraw(canvas: Canvas) {
    path.addCircle(width / 2f, height / 2f, (min(width, height) / 4f) * scaleFactor,
      Path.Direction.CW)
    canvas.execute {
      clipPath(path)
      super.onDraw(canvas)
    }
    path.reset()
  }
}