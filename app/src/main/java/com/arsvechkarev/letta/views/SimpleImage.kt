package com.arsvechkarev.letta.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.graphics.LIGHT_GRAY
import com.arsvechkarev.letta.utils.extenstions.dp
import com.arsvechkarev.letta.utils.extenstions.execute
import com.arsvechkarev.letta.utils.extenstions.toBitmap

class SimpleImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
  }
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeWidth = 0.5f.dp
    color = LIGHT_GRAY
  }
  private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
  }
  private val image: Bitmap?
  private val imageRect = Rect()
  
  private var scaleFactor = 1f
  private val animator = ValueAnimator()
  
  init {
    val arr = context.obtainStyledAttributes(attrs, R.styleable.SimpleImage, 0, 0)
    image = arr.getDrawable(R.styleable.SimpleImage_imageSrc)?.toBitmap()
    arr.recycle()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    imageRect.set(paddingLeft, paddingTop, w - paddingRight, h - paddingBottom)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = image?.width ?: 0
    val size = width + paddingStart + paddingEnd
    setMeasuredDimension(
      resolveSize(size, widthMeasureSpec),
      resolveSize(size, widthMeasureSpec)
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    image ?: return
    canvas.execute {
      val halfWidth = width / 2f
      val halfHeight = height / 2f
      scale(scaleFactor, scaleFactor, halfWidth, halfHeight)
      drawCircle(halfWidth, halfHeight, halfWidth, backgroundPaint)
      drawBitmap(image, null, imageRect, imagePaint)
      drawCircle(halfWidth, halfHeight, halfWidth, strokePaint)
    }
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        animate(down = true)
        return true
      }
      MotionEvent.ACTION_UP -> {
        animate(down = false)
        performClick()
        return true
      }
    }
    return false
  }
  
  private fun animate(down: Boolean = true) {
    val endScale = if (down) 1.2f else 1.0f
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