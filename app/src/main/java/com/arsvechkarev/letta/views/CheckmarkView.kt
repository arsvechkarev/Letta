package com.arsvechkarev.letta.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.animations.DURATION_DEFAULT
import com.arsvechkarev.letta.animations.DURATION_SMALL
import com.arsvechkarev.letta.utils.cancelIfRunning
import com.arsvechkarev.letta.utils.doOnEnd
import com.arsvechkarev.letta.utils.drawBounds
import com.arsvechkarev.letta.utils.i
import com.arsvechkarev.letta.utils.stopIfRunning

class CheckmarkView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = 0xFF00CC2C.toInt()
    style = Paint.Style.STROKE
  }
  
  private val checkmarkAppear = context.getDrawable(
    R.drawable.avd_chechmark_appear) as AnimatedVectorDrawable
  
  private val checkmarkDisappear = context.getDrawable(
    R.drawable.avd_chechmark_disappear) as AnimatedVectorDrawable
  
  private var currentDrawable = checkmarkDisappear
  
  init {
    val colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    checkmarkAppear.colorFilter = colorFilter
    checkmarkDisappear.colorFilter = colorFilter
  }
  
  private var maxStrokeWidth = -1f
  private var isClicked = true
  
  private val animator = ValueAnimator().apply {
    duration = DURATION_SMALL
    interpolator = AccelerateDecelerateInterpolator()
    addUpdateListener {
      circlePaint.strokeWidth = it.animatedValue as Float
      invalidate()
    }
  }
  
  init {
    isClickable = true
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    maxStrokeWidth = maxOf(w, h) / 2f
    circlePaint.strokeWidth = maxStrokeWidth
    val bounds = Rect(
      (w / 2f - w * 0.4f).i,
      (h / 2f - h * 0.4f).i,
      (w / 2f + w * 0.4f).i,
      (h / 2f + h * 0.4f).i
    )
    checkmarkAppear.bounds = bounds
    checkmarkDisappear.bounds = bounds
  }
  
  override fun onDraw(canvas: Canvas) {
    if (circlePaint.strokeWidth != 0f) {
      canvas.drawCircle(width / 2f, height / 2f,
        maxOf(width / 2f, height / 2f) - circlePaint.strokeWidth / 2, circlePaint)
    }
    currentDrawable.draw(canvas)
  }
  
  override fun performClick(): Boolean {
    if (animator.isRunning) {
      return false
    }
    if (isClicked) {
      currentDrawable = checkmarkDisappear
      animator.setFloatValues(circlePaint.strokeWidth, 0f)
    } else {
      currentDrawable = checkmarkAppear
      animator.setFloatValues(circlePaint.strokeWidth, maxStrokeWidth)
    }
    isClicked = !isClicked
    currentDrawable.start()
    animator.start()
    return super.performClick()
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    animator.cancelIfRunning()
    currentDrawable.stopIfRunning()
  }
}