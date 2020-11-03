package com.arsvechkarev.letta.views.behaviors

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.core.DURATION_SHORT
import com.arsvechkarev.letta.extensions.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.extensions.assertThat
import com.arsvechkarev.letta.extensions.doOnEnd
import com.arsvechkarev.letta.extensions.getBehavior

/**
 * Behavior for header view in coordinator layout
 */
class HeaderBehavior(context: Context, attrs: AttributeSet? = null) :
  CoordinatorLayout.Behavior<View>() {
  
  private var headerViewOffsetHelper: HeaderViewOffsetHelper? = null
  private var offsetFromPreviousLayout = 0
  private var alreadyStartedScrolling = false
  
  private val scrollAnimator = ValueAnimator().apply {
    duration = DURATION_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      val offset = it.animatedValue as Int
      updateTopBottomOffset(headerViewOffsetHelper!!.topAndBottomOffset - offset)
    }
  }
  
  /**
   * Returns minimum height the header can have taking into account [slideRangeCoefficient]
   */
  val minHeight: Int
    get() {
      if (slideRangeCoefficient == 1f) {
        slideRangeCoefficient = calculateSlideRangeCoefficient()
      }
      return ((headerViewOffsetHelper?.view?.height ?: 0) * (1f - slideRangeCoefficient)).toInt()
    }
  
  /**
   * Calculates [slideRangeCoefficient] when ready
   */
  var calculateSlideRangeCoefficient: () -> Float = { 1f }
  
  /**
   * Determines how much slide range should be squashed compared to header height
   *
   * Example: if header height is 400 and slideRangeCoefficient is 0.8, total range
   * would be 400 * 0.8 = 320
   */
  var slideRangeCoefficient = 1f
    set(value) {
      assertThat(value in 0f..1f) { "Range should be in range 0..1" }
      headerViewOffsetHelper?.slideRangeCoefficient = value
      field = value
    }
  
  /**
   * Determines whether the header should respond to touch events
   */
  var isScrollable: Boolean = true
  
  /**
   * Smoothly scrolls header view to initial position
   */
  fun animateScrollToTop(andThen: () -> Unit = {}) {
    if (offsetFromPreviousLayout != 0) {
      scrollAnimator.setIntValues(offsetFromPreviousLayout, 0)
      scrollAnimator.start()
      scrollAnimator.doOnEnd(andThen)
    } else {
      andThen()
    }
  }
  
  override fun onLayoutChild(parent: CoordinatorLayout,
                             child: View, layoutDirection: Int): Boolean {
    offsetFromPreviousLayout = headerViewOffsetHelper?.topAndBottomOffset ?: 0
    if (headerViewOffsetHelper == null) {
      if (slideRangeCoefficient == 1f) {
        slideRangeCoefficient = calculateSlideRangeCoefficient()
      }
      headerViewOffsetHelper = HeaderViewOffsetHelper(parent, child, slideRangeCoefficient)
    }
    parent.onLayoutChild(child, layoutDirection)
    ViewCompat.offsetTopAndBottom(child, offsetFromPreviousLayout)
    return true
  }
  
  override fun onStartNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: View,
    directTargetChild: View,
    target: View,
    axes: Int,
    type: Int
  ): Boolean {
    if (alreadyStartedScrolling && allowScrolling) return true
    assertThat(target is RecyclerView)
    if (target.allowRecyclerScrolling()) {
      alreadyStartedScrolling = true
      return true
    }
    return allowScrolling
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
    assertThat(target is RecyclerView)
    val targetViewOffset = target.computeVerticalScrollOffset()
    if (alreadyStartedScrolling && allowScrolling && targetViewOffset == 0) {
      consumed[1] = updateTopBottomOffset(dy)
    } else if (allowScrolling && targetViewOffset == 0 && target.allowRecyclerScrolling()) {
      consumed[1] = updateTopBottomOffset(dy)
    }
  }
  
  override fun onNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: View,
    target: View,
    dxConsumed: Int,
    dyConsumed: Int,
    dxUnconsumed: Int,
    dyUnconsumed: Int,
    type: Int,
    consumed: IntArray
  ) {
    assertThat(target is RecyclerView)
    if (alreadyStartedScrolling && allowScrolling && dyUnconsumed < 0) {
      consumed[1] = updateTopBottomOffset(dyUnconsumed)
    } else if (allowScrolling && dyUnconsumed < 0 && target.allowRecyclerScrolling()) {
      consumed[1] = updateTopBottomOffset(dyUnconsumed)
    }
  }
  
  private fun updateTopBottomOffset(dy: Int): Int {
    return headerViewOffsetHelper!!.updateOffset(dy)
  }
  
  private val allowScrolling get() = !scrollAnimator.isRunning && isScrollable
  
  companion object {
    
    val View.asHeader get() = getBehavior<HeaderBehavior>()
  }
}