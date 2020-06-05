package com.arsvechkarev.letta.views.behaviors

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.arsvechkarev.letta.core.DURATION_MEDIUM
import com.arsvechkarev.letta.utils.cancelIfRunning
import java.lang.ref.WeakReference

class BottomSheetBehavior<V : View>() : CoordinatorLayout.Behavior<V>() {
  
  private var height = -1
  private var currentState = State.COLLAPSED
  
  private var viewReference: WeakReference<V>? = null
  private var currentOffset = 0
  private val animator = ValueAnimator().apply {
    duration = DURATION_MEDIUM
    interpolator = AccelerateDecelerateInterpolator()
    addUpdateListener {
      val child = viewReference?.get() ?: return@addUpdateListener
      val previousOffset = currentOffset
      currentOffset = it.animatedValue as Int
      ViewCompat.offsetTopAndBottom(child, currentOffset - previousOffset)
    }
  }
  
  constructor(context: Context, attrs: AttributeSet) : this()
  
  val isShown: Boolean get() = currentState == State.EXPANDED
  
  fun show() {
    setState(State.EXPANDED)
  }
  
  fun hide() {
    setState(State.COLLAPSED)
  }
  
  fun attachShadowView(view: View) {
  
  }
  
  override fun onLayoutChild(
    parent: CoordinatorLayout,
    child: V,
    layoutDirection: Int
  ): Boolean {
    parent.onLayoutChild(child, layoutDirection)
    
    if (viewReference == null) {
      height = child.height
      viewReference = WeakReference(child)
    }
    
    if (currentState == State.COLLAPSED) {
      currentOffset = height
      ViewCompat.offsetTopAndBottom(child, height)
    }
    return true
  }
  
  override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
    viewReference = null
  }
  
  override fun onDetachedFromLayoutParams() {
    viewReference = null
  }
  
  private fun setState(newState: State) {
    if (currentState == newState) return
    currentState = newState
    
    when (newState) {
      State.EXPANDED -> {
        animateTo(0)
      }
      State.COLLAPSED -> {
        animateTo(height)
      }
    }
  }
  
  private fun animateTo(offset: Int) {
    animator.cancelIfRunning()
    animator.setIntValues(currentOffset, offset)
    animator.start()
  }
  
  enum class State {
    EXPANDED, COLLAPSED
  }
}