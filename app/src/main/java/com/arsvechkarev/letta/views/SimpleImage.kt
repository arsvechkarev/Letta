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
import com.arsvechkarev.letta.utils.contains
import com.arsvechkarev.letta.utils.execute
import com.arsvechkarev.letta.utils.STROKE_PAINT

class SimpleImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
  }
  private val image: Drawable
  private var scaleFactor = 1f
  private val animator = ValueAnimator()
  
  init {
    val arr = context.obtainStyledAttributes(attrs, R.styleable.SimpleImage, 0, 0)
    image = arr.getDrawable(R.styleable.SimpleImage_imageSrc)!!.mutate()
    image.colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
    arr.recycle()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    image.setBounds(paddingLeft, paddingTop, w - paddingRight, h - paddingBottom)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val max = maxOf(image.intrinsicWidth, image.intrinsicHeight)
    val size = max + paddingStart + paddingEnd
    setMeasuredDimension(
      resolveSize(size, widthMeasureSpec),
      resolveSize(size, widthMeasureSpec)
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.execute {
      val halfWidth = width / 2f
      val halfHeight = height / 2f
      scale(scaleFactor, scaleFactor, halfWidth, halfHeight)
      drawCircle(halfWidth, halfHeight, halfWidth, backgroundPaint)
      image.draw(canvas)
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