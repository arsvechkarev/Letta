package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.extensions.f

class GradientHeaderView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val primaryColor = ContextCompat.getColor(context, R.color.primary)
  private val secondaryColor = ContextCompat.getColor(context, R.color.primary_light)
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val path = Path()
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val offset = h / 4f
    path.reset()
    path.moveTo(0f, 0f)
    path.lineTo(w.f, 0f)
    path.lineTo(w.f, h - offset)
    path.quadTo(w / 2f, h.f, 0f, h - offset)
    path.close()
    
    paint.shader = LinearGradient(
      0f, h - offset, w.f - w.f / 4 , 0f,
      intArrayOf(primaryColor, secondaryColor), null,
      Shader.TileMode.CLAMP
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawPath(path, paint)
  }
}