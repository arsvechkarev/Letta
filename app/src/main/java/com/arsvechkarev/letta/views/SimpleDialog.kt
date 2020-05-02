package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.FrameLayout
import com.arsvechkarev.letta.animations.DURATION_DEFAULT
import com.arsvechkarev.letta.animations.animateColor
import com.arsvechkarev.letta.utils.assertThat
import com.arsvechkarev.letta.utils.gone
import com.arsvechkarev.letta.utils.visible

class SimpleDialog @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
  
  private lateinit var dialogView: View
  private var wasNoMoveEvent = false
  
  init {
    gone()
  }
  
  override fun onFinishInflate() {
    super.onFinishInflate()
    assertThat(childCount == 1) { "Only one child for dialog is allowed" }
    dialogView = getChildAt(0)
  }
  
  fun show() {
    post {
      visible()
      dialogView.alpha = 0f
      dialogView.visible()
      animateColor(0x00000000, 0x70000000)
      dialogView.animate()
          .alpha(1f)
          .setDuration(DURATION_DEFAULT)
          .start()
    }
  }
  
  fun dismiss() {
    post {
      animateColor(0x70000000, 0x00000000, andThen = { gone() })
      dialogView.animate()
          .alpha(0f)
          .setDuration(DURATION_DEFAULT)
          .start()
    }
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
        val eventInDialog = event.x > dialogView.left && event.x < dialogView.right
            && event.y > dialogView.top && event.y < dialogView.bottom
        if (wasNoMoveEvent && !eventInDialog) {
          dismiss()
          return true
        }
      }
    }
    return false
  }
}
