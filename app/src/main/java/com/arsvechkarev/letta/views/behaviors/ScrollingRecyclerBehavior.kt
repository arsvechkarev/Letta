package com.arsvechkarev.letta.views.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.arsvechkarev.letta.views.HeaderFrameLayout

class ScrollingRecyclerBehavior<V : View>() : CoordinatorLayout.Behavior<V>() {
  
  private var offset: Int = -1
  private var offsettingFirstTime = true
  
  constructor(context: Context, attrs: AttributeSet) : this()
  
  override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
    return dependency is HeaderFrameLayout
  }
  
  override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
    offset = if (offsettingFirstTime) dependency.bottom else dependency.bottom - child.top
    offsettingFirstTime = false
    ViewCompat.offsetTopAndBottom(child, offset)
    return true
  }
  
  override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
    parent.onLayoutChild(child, layoutDirection)
    ViewCompat.offsetTopAndBottom(child, offset)
    return true
  }
}