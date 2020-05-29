package com.arsvechkarev.letta.views.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import com.arsvechkarev.letta.LettaApplication
import kotlin.math.abs
import kotlin.math.roundToInt

open class HeaderBehavior<V : View>() : CoordinatorLayout.Behavior<V>() {
  
  private val scroller = OverScroller(LettaApplication.appContext)
  private var viewOffsetHelper: ViewOffsetHelper? = null
  private var velocityTracker: VelocityTracker? = null
  private var flingRunnable: Runnable? = null
  
  private var isBeingDragged = false
  private var activePointerId = INVALID_POINTER
  private var lastMotionY = 0
  private var touchSlop = -1
  
  constructor(context: Context, attrs: AttributeSet) : this()
  
  override fun onLayoutChild(
    parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
    parent.onLayoutChild(child, layoutDirection)
    if (viewOffsetHelper == null) {
      viewOffsetHelper = ViewOffsetHelper(child)
    }
    viewOffsetHelper!!.onViewLayout()
    return true
  }
  
  override fun onInterceptTouchEvent(
    parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
    if (touchSlop < 0) {
      touchSlop = ViewConfiguration.get(parent.context).scaledTouchSlop
    }
    val action = ev.action
    
    if (action == ACTION_MOVE && isBeingDragged) {
      return true
    }
    when (ev.actionMasked) {
      ACTION_DOWN -> {
        isBeingDragged = false
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        if (parent.isPointInChildBounds(child, x, y)) {
          lastMotionY = y
          activePointerId = ev.getPointerId(0)
          ensureVelocityTracker()
        }
      }
      ACTION_MOVE -> {
        val activePointerId = activePointerId
        if (activePointerId == INVALID_POINTER) {
          // If we don't have a valid id, the touch down wasn't on content.
          return false
        }
        val pointerIndex = ev.findPointerIndex(activePointerId)
        if (pointerIndex == -1) {
          return false
        }
        val y = ev.getY(pointerIndex).toInt()
        val yDiff = Math.abs(y - lastMotionY)
        if (yDiff > touchSlop) {
          isBeingDragged = true
          lastMotionY = y
        }
      }
      ACTION_CANCEL, ACTION_UP -> {
        endTouch()
      }
    }
    if (velocityTracker != null) {
      velocityTracker!!.addMovement(ev)
    }
    return isBeingDragged
  }
  
  override fun onTouchEvent(
    parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
    if (touchSlop < 0) {
      touchSlop = ViewConfiguration.get(parent.context).scaledTouchSlop
    }
    when (ev.actionMasked) {
      ACTION_DOWN -> {
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        if (parent.isPointInChildBounds(child, x, y)) {
          lastMotionY = y
          activePointerId = ev.getPointerId(0)
          ensureVelocityTracker()
        } else {
          return false
        }
      }
      ACTION_MOVE -> {
        val activePointerIndex = ev.findPointerIndex(activePointerId)
        if (activePointerIndex == -1) {
          return false
        }
        val y = ev.getY(activePointerIndex).toInt()
        var dy = lastMotionY - y
        if (!isBeingDragged && abs(dy) > touchSlop) {
          isBeingDragged = true
          if (dy > 0) {
            dy -= touchSlop
          } else {
            dy += touchSlop
          }
        }
        if (isBeingDragged) {
          lastMotionY = y
          updateTopBottomOffset(dy)
        }
      }
      ACTION_UP -> {
        if (velocityTracker != null) {
          velocityTracker!!.addMovement(ev)
          velocityTracker!!.computeCurrentVelocity(1000)
          val velocityY = velocityTracker!!.getYVelocity(activePointerId)
          fling(child, velocityY)
        }
        endTouch()
      }
      ACTION_CANCEL -> {
        endTouch()
      }
    }
    if (velocityTracker != null) {
      velocityTracker!!.addMovement(ev)
    }
    return true
  }
  
  override fun onStartNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: V,
    directTargetChild: View,
    target: View,
    axes: Int,
    type: Int
  ): Boolean {
    return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
  }
  
  override fun onNestedPreScroll(
    coordinatorLayout: CoordinatorLayout,
    child: V,
    target: View,
    dx: Int,
    dy: Int,
    consumed: IntArray,
    type: Int
  ) {
    val targetViewOffset = (target as? ScrollingView)?.computeVerticalScrollOffset() ?: 0
    if (targetViewOffset == 0) {
      consumed[1] = updateTopBottomOffset(dy)
    }
  }
  
  override fun onNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: V,
    target: View,
    dxConsumed: Int,
    dyConsumed: Int,
    dxUnconsumed: Int,
    dyUnconsumed: Int,
    type: Int,
    consumed: IntArray
  ) {
    if (dyUnconsumed < 0) {
      consumed[1] = updateTopBottomOffset(dyUnconsumed)
    }
  }
  
  protected fun updateTopBottomOffset(dy: Int): Int {
    val prefOffset = topAndBottomOffset
    if (dy != 0) {
      val resultOffset = (topAndBottomOffset - dy).coerceIn(maxScrollingOffset, 0)
      viewOffsetHelper!!.setTopAndBottomOffset(resultOffset)
    }
    return prefOffset - topAndBottomOffset
  }
  
  private fun fling(
    child: V,
    velocityY: Float) {
    if (flingRunnable != null) {
      child.removeCallbacks(flingRunnable)
      flingRunnable = null
    }
    scroller.fling(
      0,
      topAndBottomOffset,
      0,
      velocityY.roundToInt(),
      0,
      0,
      maxScrollingOffset,
      0
    )
    if (scroller.computeScrollOffset()) {
      flingRunnable = FlingRunnable(child)
      ViewCompat.postOnAnimation(child, flingRunnable)
    }
  }
  
  private fun ensureVelocityTracker() {
    if (velocityTracker == null) {
      velocityTracker = VelocityTracker.obtain()
    }
  }
  
  private fun endTouch() {
    isBeingDragged = false
    activePointerId = INVALID_POINTER
    if (velocityTracker != null) {
      velocityTracker!!.recycle()
      velocityTracker = null
    }
  }
  
  private val topAndBottomOffset: Int
    get() = viewOffsetHelper!!.topAndBottomOffset
  
  private val maxScrollingOffset: Int
    get() = -viewOffsetHelper!!.view.height
  
  private inner class FlingRunnable(val child: V) : Runnable {
    
    override fun run() {
      if (scroller.computeScrollOffset()) {
        updateTopBottomOffset(topAndBottomOffset - scroller.currY)
        child.postOnAnimation(this)
      }
    }
  }
  
  companion object {
    private const val INVALID_POINTER = -1
  }
}