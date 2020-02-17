package com.arsvechkarev.letta.animations

import android.view.View
import com.arsvechkarev.letta.utils.gone
import com.arsvechkarev.letta.utils.visible

fun View.animateFadeIn() {
  alpha = 0f
  visible()
  animate()
      .configure()
      .alpha(1f)
      .withEndAction { isClickable = true }
      .start()
}

fun View.animateFadeOut(andThen: () -> Unit = {}) {
  isClickable = false
  animate()
      .configure()
      .alpha(0f)
      .withEndAction {
        gone()
        andThen()
      }
      .start()
}
