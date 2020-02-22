package com.arsvechkarev.letta.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.i
import com.arsvechkarev.letta.utils.toBitmap

class OutlinedImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  companion object {
    const val DEFAULT_PRIMARY = Color.WHITE
    const val DEFAULT_SECONDARY = Color.BLACK
  }
  
  private var primaryColor = DEFAULT_PRIMARY
  private var secondaryColor = DEFAULT_SECONDARY
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = primaryColor
    style = Paint.Style.STROKE
  }
  private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP)
  }
  private val image: Bitmap?
  private val imageRect = RectF()
  
  private var imageSize = 0f
  private var innerPadding = 0f
  private var inverseColorsOnClick = false
  
  private var scaleFactor = 1f
  private var colorsAreReversed = false
  
  private val animator = ValueAnimator()
  
  init {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OutlinedImage, 0, 0)
    image = typedArray.getDrawable(R.styleable.OutlinedImage_image)?.toBitmap()
    innerPadding = typedArray.getDimension(R.styleable.OutlinedImage_innerPadding, 5.dp)
    strokePaint.strokeWidth = typedArray.getDimension(R.styleable.OutlinedImage_strokeWidth, 2.dp)
    inverseColorsOnClick = typedArray.getBoolean(R.styleable.OutlinedImage_inverseColorsOnClick,
      false)
    typedArray.recycle()
    
    imageSize = image?.width?.f ?: 0f
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val size = imageSize + innerPadding * 2 + strokePaint.strokeWidth * 2 + paddingTop * 2
    setMeasuredDimension(resolveSize(size.i, widthMeasureSpec),
      resolveSize(size.i, widthMeasureSpec))
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    imageRect.set(innerPadding, innerPadding, w - innerPadding, h - innerPadding)
  }
  
  override fun onDraw(canvas: Canvas) {
    with(canvas) {
      save()
      scale(scaleFactor, scaleFactor, width / 2f, width / 2f)
      drawCircle(width / 2f, height / 2f, width / 2f - strokePaint.strokeWidth, strokePaint)
      if (image != null) drawBitmap(image, null, imageRect, imagePaint)
      restore()
    }
  }
  
  override fun performClick(): Boolean {
    if (inverseColorsOnClick) {
      val currentImageColor: Int
      if (colorsAreReversed) {
        currentImageColor = primaryColor
        strokePaint.style = Paint.Style.STROKE
      } else {
        currentImageColor = secondaryColor
        strokePaint.style = Paint.Style.FILL_AND_STROKE
      }
      imagePaint.colorFilter = PorterDuffColorFilter(currentImageColor, PorterDuff.Mode.SRC_ATOP)
      colorsAreReversed = !colorsAreReversed
      invalidate()
    }
    return super.performClick()
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        animate(down = true)
        return true
      }
      ACTION_UP -> {
        animate(down = false)
        performClick()
        return true
      }
    }
    return false
  }
  
  private fun animate(down: Boolean = true) {
    val endScale = if (down) 1.1f else 1.0f
    with(animator) {
      cancel()
      setFloatValues(scaleFactor, endScale)
      duration = 50
      addUpdateListener {
        scaleFactor = animatedValue as Float
        invalidate()
      }
      start()
    }
  }
}