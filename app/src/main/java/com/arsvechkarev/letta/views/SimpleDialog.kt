package com.arsvechkarev.letta.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.DURATION_DEFAULT
import com.arsvechkarev.letta.core.DURATION_MEDIUM
import com.arsvechkarev.letta.extensions.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.extensions.cancelIfRunning
import com.arsvechkarev.letta.extensions.contains
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.extensions.gone
import com.arsvechkarev.letta.extensions.visible

class SimpleDialog @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  
  private var wasNoMoveEvent = false
  private var currentShadowFraction = 0f
  private val dialogView get() = getChildAt(0)
  private val shadowAnimator = ValueAnimator().apply {
    interpolator = AccelerateDecelerateInterpolator
    duration = DURATION_MEDIUM
    addUpdateListener {
      currentShadowFraction = it.animatedValue as Float
      onShadowFractionChangedListener?.invoke(currentShadowFraction)
      val color = ColorUtils.blendARGB(Color.TRANSPARENT, Colors.Shadow, currentShadowFraction)
      setBackgroundColor(color)
    }
  }
  
  var onShadowFractionChangedListener: ((Float) -> Unit)? = null
  
  var isOpened = false
    private set
  
  init {
    gone()
  }
  
  fun show() {
    if (isOpened) return
    isOpened = true
    post {
      visible()
      dialogView.alpha = 0f
      dialogView.visible()
      shadowAnimator.cancelIfRunning()
      shadowAnimator.setFloatValues(currentShadowFraction, 1f)
      shadowAnimator.start()
      dialogView.animate()
          .withLayer()
          .alpha(1f)
          .translationY(0f)
          .setDuration(DURATION_DEFAULT)
          .setInterpolator(AccelerateDecelerateInterpolator)
          .start()
    }
  }
  
  fun hide() {
    if (!isOpened) return
    isOpened = false
    post {
      shadowAnimator.cancelIfRunning()
      shadowAnimator.setFloatValues(currentShadowFraction, 0f)
      shadowAnimator.start()
      dialogView.animate()
          .withLayer()
          .alpha(0f)
          .translationY(getTranslationForDialogView())
          .setDuration(DURATION_DEFAULT)
          .setInterpolator(AccelerateDecelerateInterpolator)
          .withEndAction(::gone)
          .start()
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    dialogView.translationY = getTranslationForDialogView()
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        wasNoMoveEvent = true
        return true
      }
      ACTION_MOVE -> {
        wasNoMoveEvent = false
      }
      ACTION_UP -> {
        if (wasNoMoveEvent && event !in dialogView) {
          hide()
          return true
        }
      }
    }
    return false
  }
  
  private fun getTranslationForDialogView(): Float = dialogView.measuredHeight.f
}