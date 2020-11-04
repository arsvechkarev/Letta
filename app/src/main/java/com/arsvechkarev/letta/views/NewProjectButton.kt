package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.extensions.findParent
import com.arsvechkarev.letta.views.behaviors.allowRecyclerScrolling
import kotlin.math.abs

class NewProjectButton @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ImageButton(context, attrs, defStyleAttr) {
  
  private val customCoordinatorLayout
    get() = findParent { it is CustomCoordinatorLayout } as CustomCoordinatorLayout
  
  private var isAnimating = false
  private var latestY = 0f
  
  var allowAnimating = true
  
  private val motionListener: (MotionEvent) -> Unit = { event ->
    when (event.action) {
      ACTION_DOWN -> {
        latestY = event.y
      }
      ACTION_MOVE -> {
        val recycler = customCoordinatorLayout.findViewById<RecyclerView>(R.id.recyclerAllProjects)
        val dy = event.y - latestY
        if (recycler.allowRecyclerScrolling()
            && abs(dy) > ViewConfiguration.get(context).scaledTouchSlop) {
          if (dy < 0) {
            animate(down = true)
          } else {
            animate(down = false)
          }
        }
      }
    }
  }
  
  fun animate(down: Boolean) {
    if (isAnimating || !allowAnimating) return
    val range = getRange()
    if (down) {
      if (translationY <= 0f) {
        performAnimation(range)
      }
    } else {
      if (translationY > 0f) {
        performAnimation(-range)
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
  
  private fun performAnimation(translation: Float) {
    isAnimating = true
    animate()
        .translationYBy(translation)
        .withEndAction { isAnimating = false }
        .start()
  }
  
  private fun getRange(): Float {
    return height * 1.5f
  }
}
