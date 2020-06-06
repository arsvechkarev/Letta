package com.arsvechkarev.letta.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import androidx.appcompat.widget.AppCompatImageView
import com.arsvechkarev.letta.core.DURATION_ON_CLICK
import com.arsvechkarev.letta.core.VIEW_CLICK_SCALE_FACTOR
import com.arsvechkarev.letta.extensions.execute
import kotlin.math.max

class ClickableSquareImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
  
  private var scaleFactor = 1f
  private val scaleAnimator = ValueAnimator()
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val height = MeasureSpec.getSize(heightMeasureSpec)
    val size = max(width, height)
    setMeasuredDimension(
      resolveSize(size, widthMeasureSpec),
      resolveSize(size, heightMeasureSpec)
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.execute {
      scale(scaleFactor, scaleFactor, width / 2f, height / 2f)
      super.onDraw(canvas)
    }
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    super.onTouchEvent(event)
    when (event.action) {
      ACTION_DOWN -> {
        animate(down = true)
        return true
      }
      ACTION_UP, ACTION_CANCEL -> {
        animate(down = false)
        return true
      }
    }
    return false
  }
  
  private fun animate(down: Boolean = true) {
    val endScale = if (down) VIEW_CLICK_SCALE_FACTOR else 1.0f
    with(scaleAnimator) {
      cancel()
      setFloatValues(scaleFactor, endScale)
      duration = DURATION_ON_CLICK
      addUpdateListener {
        scaleFactor = animatedValue as Float
        invalidate()
      }
      start()
    }
  }
}