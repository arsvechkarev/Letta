package com.arsvechkarev.letta.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.STROKE_PAINT
import com.arsvechkarev.letta.utils.contains
import com.arsvechkarev.letta.utils.execute

class Image @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
  }
  private var image: Drawable?
  private val animator = ValueAnimator()
  
  private var scaleFactor = 1f
  
  init {
    val arr = context.obtainStyledAttributes(attrs, R.styleable.Image, defStyleAttr, 0)
    image = arr.getDrawable(R.styleable.Image_imageSrc)?.mutate()
    image?.colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
    arr.recycle()
  }
  
  fun setImage(drawable: Drawable) {
    image = drawable
    requestLayout()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val max = maxOf(image?.intrinsicWidth ?: 0, image?.intrinsicHeight ?: 0)
    val size = max + paddingStart + paddingEnd
    setMeasuredDimension(
      resolveSize(size, widthMeasureSpec),
      resolveSize(size, heightMeasureSpec)
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    if (image?.bounds?.width() == 0) {
      image?.setBounds(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)
    }
    canvas.execute {
      val halfWidth = width / 2f
      val halfHeight = height / 2f
      scale(scaleFactor, scaleFactor, halfWidth, halfHeight)
      drawCircle(halfWidth, halfHeight, halfWidth, backgroundPaint)
      image?.draw(canvas)
      drawCircle(halfWidth, halfHeight, halfWidth,
        STROKE_PAINT)
    }
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        animate(down = true)
        return true
      }
      MotionEvent.ACTION_UP -> {
        if (event in this) {
          performClick()
        }
        animate(down = false)
        return true
      }
    }
    return false
  }
  
  private fun animate(down: Boolean = true) {
    val endScale = if (down) 0.9f else 1.0f
    with(animator) {
      cancel()
      setFloatValues(scaleFactor, endScale)
      duration = 40
      addUpdateListener {
        scaleFactor = animatedValue as Float
        invalidate()
      }
      start()
    }
  }
}