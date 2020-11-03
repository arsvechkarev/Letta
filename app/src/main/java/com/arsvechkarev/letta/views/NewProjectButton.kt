package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import com.arsvechkarev.letta.extensions.findParent

class NewProjectButton @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ImageButton(context, attrs, defStyleAttr) {
  
  private val customCoordinatorLayout
    get() = findParent { it is CustomCoordinatorLayout } as CustomCoordinatorLayout
  
  private var isAnimating = false
  private var latestY = 0f
  
  private val motionListener: (MotionEvent) -> Unit = { event ->
    when (event.action) {
      ACTION_DOWN -> {
        latestY = event.y
      }
      ACTION_MOVE -> {
        val dy = event.y - latestY
        if (dy < 0) {
          animate(isScrollingDown = true)
        } else {
          animate(isScrollingDown = false)
        }
      }
    }
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    customCoordinatorLayout.addMotionEventListener(motionListener)
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    customCoordinatorLayout.removeMotionEventListener(motionListener)
  }
  
  private fun animate(isScrollingDown: Boolean) {
    if (isAnimating) return
    val range = getRange()
    if (isScrollingDown) {
      if (translationY <= 0f) {
        performAnimation(range)
      }
    } else {
      if (translationY > 0f) {
        performAnimation(-range)
      }
    }
  }
  
  private fun performAnimation(translation: Float) {
    isAnimating = true
    animate()
        .yBy(translation)
        .withEndAction { isAnimating = false }
        .start()
  }
  
  private fun getRange(): Float {
    return height * 1.5f
  }
}
