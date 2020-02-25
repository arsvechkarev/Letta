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
import com.arsvechkarev.letta.graphics.LIGHT_GRAY
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.execute
import com.arsvechkarev.letta.utils.i
import com.arsvechkarev.letta.utils.toBitmap

class OutlinedImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
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
  private val outerStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = LIGHT_GRAY
    strokeWidth = 0.5f.dp
    style = Paint.Style.STROKE
  }
  private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP)
  }
  private val imageStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    colorFilter = PorterDuffColorFilter(LIGHT_GRAY, PorterDuff.Mode.SRC_ATOP)
  }
  private val image: Bitmap?
  private val imageStroke: Bitmap?
  private val imageRect = RectF()
  
  private var innerPadding = 0f
  private var autoInverse = false
  
  private var scaleFactor = 1f
  private var colorsAreReversed = false
  
  private val animator = ValueAnimator()
  
  init {
    val arr = context.obtainStyledAttributes(attrs, R.styleable.OutlinedImage, 0, 0)
    image = arr.getDrawable(R.styleable.OutlinedImage_image)?.toBitmap()
    imageStroke = arr.getDrawable(R.styleable.OutlinedImage_imageStroke)?.toBitmap()
    innerPadding = arr.getDimension(R.styleable.OutlinedImage_innerPadding, 5.dp)
    strokePaint.strokeWidth = arr.getDimension(R.styleable.OutlinedImage_strokeWidth, 2.dp)
    autoInverse = arr.getBoolean(
      R.styleable.OutlinedImage_autoInverse,
      false
    )
    arr.recycle()
  }
  
  fun inverse() {
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
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    imageRect.set(innerPadding, innerPadding, w - innerPadding, h - innerPadding)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = image?.width ?: 0
    val size = width + innerPadding * 2 + strokePaint.strokeWidth * 2 + paddingStart + paddingEnd
    setMeasuredDimension(
      resolveSize(size.i, widthMeasureSpec),
      resolveSize(size.i, widthMeasureSpec)
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    image ?: return
    canvas.execute {
      val halfWidth = width / 2f
      val halfHeight = height / 2f
      scale(scaleFactor, scaleFactor, halfWidth, halfWidth)
      drawCircle(halfWidth, halfHeight, halfWidth - strokePaint.strokeWidth, strokePaint)
      drawCircle(halfWidth, halfHeight, halfWidth - strokePaint.strokeWidth / 2, outerStrokePaint)
      if (!colorsAreReversed) {
        drawCircle(halfWidth, halfHeight, halfWidth - strokePaint.strokeWidth * 1.6f,
          outerStrokePaint)
      }
      drawBitmap(image, null, imageRect, imagePaint)
      if (imageStroke != null) {
        drawBitmap(imageStroke, null, imageRect, imageStrokePaint)
      }
    }
  }
  
  override fun performClick(): Boolean {
    if (autoInverse) {
      inverse()
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