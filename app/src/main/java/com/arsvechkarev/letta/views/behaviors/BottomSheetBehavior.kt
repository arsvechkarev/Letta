package com.arsvechkarev.letta.views.behaviors

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.letta.core.DURATION_MEDIUM
import com.arsvechkarev.letta.utils.cancelIfRunning
import com.arsvechkarev.letta.utils.doOnEnd
import java.lang.ref.WeakReference

class BottomSheetBehavior<V : View>() : CoordinatorLayout.Behavior<V>() {
  
  private var animating = false
  private var currentState = State.COLLAPSED
  private var viewReference: WeakReference<V>? = null
  
  private var parentBottom = -1
  private var childMaxTop = -1
  
  private val slideListeners = ArrayList<(Float) -> Unit>()
  
  private val animator = ValueAnimator().apply {
    duration = DURATION_MEDIUM
    interpolator = AccelerateDecelerateInterpolator()
    addUpdateListener {
      val child = viewReference?.get() ?: return@addUpdateListener
      child.top = it.animatedValue as Int
      notifyListeners(child)
    }
  }
  
  private fun notifyListeners(child: V) {
    val percent = 1 - (child.top - childMaxTop).toFloat() / (parentBottom - childMaxTop)
    slideListeners.forEach { listener -> listener.invoke(percent) }
  }
  
  @Suppress("unused") // Accessible through xml
  constructor(context: Context, attrs: AttributeSet) : this()
  
  val isShown: Boolean get() = currentState == State.EXPANDED
  
  fun show() {
    setState(State.EXPANDED)
  }
  
  fun hide() {
    setState(State.COLLAPSED)
  }
  
  fun addSlideListener(listener: (Float) -> Unit) {
    slideListeners.add(listener)
  }
  
  override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
    return true
  }
  
  override fun onLayoutChild(
    parent: CoordinatorLayout,
    child: V,
    layoutDirection: Int
  ): Boolean {
    // If animating, do not allow parent to layout child, because it creates flicker
    if (animating) return true
    
    parent.onLayoutChild(child, layoutDirection)
    
    if (viewReference == null) {
      if (childMaxTop == -1) {
        childMaxTop = child.top
      }
      parentBottom = parent.bottom
      viewReference = WeakReference(child)
    }
    
    if (currentState == State.COLLAPSED) {
      child.top = parentBottom
    }
    return true
  }
  
  override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
    viewReference = null
  }
  
  override fun onDetachedFromLayoutParams() {
    viewReference = null
    slideListeners.clear()
  }
  
  private fun setState(newState: State) {
    if (currentState == newState) return
    currentState = newState
  
    when (newState) {
      State.EXPANDED -> {
        animateTo(childMaxTop)
      }
      State.COLLAPSED -> {
        animateTo(parentBottom)
      }
    }
  }
  
  private fun animateTo(newTop: Int) {
    animating = true
    val child = viewReference?.get()!!
    animator.cancelIfRunning()
    animator.setIntValues(child.top, newTop)
    animator.start()
    animator.doOnEnd { animating = false }
  }
  
  enum class State {
    EXPANDED, COLLAPSED
  }
}