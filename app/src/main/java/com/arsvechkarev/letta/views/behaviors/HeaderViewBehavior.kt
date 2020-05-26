package com.arsvechkarev.letta.views.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.letta.LettaApplication
import kotlin.math.abs

class HeaderViewBehavior<V : View>() : CoordinatorLayout.Behavior<V>() {
  
  constructor(context: Context, attrs: AttributeSet) : this()
  
  private var latestY = 0f
  private var touchSlop = ViewConfiguration.get(LettaApplication.appContext).scaledTouchSlop
  private var isBeingDragged = false
  
  override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
    
    if (ev.action == ACTION_MOVE && isBeingDragged) {
      return true
    }
    
    when (ev.action) {
      ACTION_DOWN -> {
        isBeingDragged = false
        if (parent.isPointInChildBounds(child, ev.x.toInt(), ev.y.toInt())) {
          latestY = ev.y
        }
      }
      ACTION_MOVE -> {
        if (abs(ev.y - latestY) > touchSlop) {
          latestY = ev.y
          isBeingDragged = true
        }
      }
      ACTION_UP, ACTION_CANCEL -> {
        isBeingDragged = false
      }
    }
    return isBeingDragged
  }
  
  override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
    var handled = false
    when (ev.action) {
      ACTION_DOWN -> {
        if (parent.isPointInChildBounds(child, ev.x.toInt(), ev.y.toInt())) {
          latestY = ev.y
          handled = true
        } else {
          handled = false
        }
      }
      ACTION_MOVE -> {
        val diff = ev.y - latestY
        latestY = ev.y
        handled = scrollHeader(parent, child, diff.toInt()) > 0
      }
    }
    println("qwerty: handled: $handled")
    return handled
  }
}