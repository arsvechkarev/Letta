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
import androidx.core.content.ContextCompat
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.DURATION_SHORT
import com.arsvechkarev.letta.core.animations.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.extensions.i

class CheckmarkView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = ContextCompat.getColor(context, R.color.light_checkmark)
    style = Paint.Style.STROKE
  }
  private val checkmarkAppear = ContextCompat.getDrawable(context,
    R.drawable.avd_chechmark_appear) as AnimatedVectorDrawable
  
  private val checkmarkDisappear = ContextCompat.getDrawable(context,
    R.drawable.avd_chechmark_disappear) as AnimatedVectorDrawable
  
  private var currentDrawable = checkmarkDisappear
  private var imageSize = 0f
  private var maxStrokeWidth = -1f
  private var circleRadius = -1f
  private var _isChecked = false
  
  var isChecked: Boolean
    get() = _isChecked
    set(value) {
      if (_isChecked == value) return
      if (maxStrokeWidth == -1f) { // Is not initialized yet
        post {
          _isChecked = value
          updateCheckedState()
        }
      } else {
        _isChecked = value
        updateCheckedState()
      }
    }
  
  init {
    val colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    checkmarkAppear.colorFilter = colorFilter
    checkmarkDisappear.colorFilter = colorFilter
  }
  
  private val animator = ValueAnimator().apply {
    duration = DURATION_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      circlePaint.strokeWidth = it.animatedValue as Float
      invalidate()
    }
  }
  
  fun updateWithoutAnimation(isChecked: Boolean) {
    if (isChecked == _isChecked) return
    _isChecked = isChecked
    if (maxStrokeWidth == -1f) {
      post {
        updateCheckedStateWithoutAnimation()
      }
    } else {
      updateCheckedStateWithoutAnimation()
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (imageSize == -1f) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    } else {
      setMeasuredDimension(
        resolveSize(imageSize.i, widthMeasureSpec),
        resolveSize(imageSize.i, heightMeasureSpec)
      )
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    circleRadius = maxOf(w / 2f, h / 2f)
    maxStrokeWidth = maxOf(w, h) / 2f
    val bounds = Rect(
      (w / 2f - w * 0.4f).i,
      (h / 2f - h * 0.4f).i,
      (w / 2f + w * 0.4f).i,
      (h / 2f + h * 0.4f).i
    )
    circlePaint.strokeWidth = if (isChecked) maxStrokeWidth else 0f
    checkmarkAppear.bounds = bounds
    checkmarkDisappear.bounds = bounds
  }
  
  override fun onDraw(canvas: Canvas) {
    if (circlePaint.strokeWidth != 0f) {
      canvas.drawCircle(width / 2f, height / 2f,
        circleRadius - circlePaint.strokeWidth / 2, circlePaint)
      currentDrawable.draw(canvas)
    }
  }
  
  private fun updateCheckedState() {
    if (animator.isRunning) {
      return
    }
    if (_isChecked) {
      currentDrawable = checkmarkAppear
      animator.setFloatValues(circlePaint.strokeWidth, maxStrokeWidth)
    } else {
      currentDrawable = checkmarkDisappear
      animator.setFloatValues(circlePaint.strokeWidth, 0f)
    }
    currentDrawable.start()
    animator.start()
  }
  
  private fun updateCheckedStateWithoutAnimation() {
    if (_isChecked) {
      circlePaint.strokeWidth = maxStrokeWidth
      currentDrawable = checkmarkAppear
    } else {
      circlePaint.strokeWidth = 0f
      currentDrawable = checkmarkDisappear
    }
    invalidate()
  }
}