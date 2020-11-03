package com.arsvechkarev.letta.views.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView

class HidingButtonBehavior(context: Context? = null, attrs: AttributeSet? = null) :
  CoordinatorLayout.Behavior<View>() {
  
  private var isAnimating = false
  private var alreadyStartedScrolling = false
  
  override fun onStartNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: View,
    directTargetChild: View,
    target: View,
    axes: Int,
    type: Int
  ): Boolean {
    if (alreadyStartedScrolling) return true
    if (target is RecyclerView) {
      if (target.allowRecyclerScrolling()) {
        alreadyStartedScrolling = true
        return true
      }
    }
    return false
  }
  
  override fun onNestedPreScroll(
    coordinatorLayout: CoordinatorLayout,
    child: View,
    target: View,
    dx: Int,
    dy: Int,
    consumed: IntArray,
    type: Int
  ) {
    val isScrollingDown = dy > 0
    animateIfNeeded(child, isScrollingDown)
  }
  
  private fun animateIfNeeded(child: View, isScrollingDown: Boolean) {
    if (isAnimating) return
    val range = getRange(child)
    if (isScrollingDown) {
      if (child.translationY <= 0f) {
        performAnimation(child, range)
      }
    } else {
      if (child.translationY > 0f) {
        performAnimation(child, -range)
      }
    }
  }
  
  private fun performAnimation(child: View, translation: Float) {
    isAnimating = true
    child.animate()
        .yBy(translation)
        .withEndAction { isAnimating = false }
        .start()
  }
  
  private fun getRange(child: View): Float {
    return child.height * 1.5f
  }
}