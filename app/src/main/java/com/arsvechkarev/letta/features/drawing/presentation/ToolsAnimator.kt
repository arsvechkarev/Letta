package com.arsvechkarev.letta.features.drawing.presentation

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.core.DURATION_DEFAULT
import com.arsvechkarev.letta.extensions.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.extensions.throwEx
import com.arsvechkarev.letta.views.ImageButton
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

class ToolsAnimator(
  private val buttonUndo: ImageButton,
  private val buttonDone: ImageButton,
  private val palette: GradientPalette,
  private val buttonSwapGradient: ImageButton,
  private val verticalSeekbar: VerticalSeekbar,
  private val recyclerBrushes: RecyclerView
) {
  
  var toolsAreVisible = true
    private set
  
  fun toggleVisibility() {
    if (toolsAreVisible) {
      animateInvisible()
    } else {
      animateVisible()
    }
    toolsAreVisible = !toolsAreVisible
  }
  
  private fun animateInvisible() {
    buttonUndo.animateInvisible { distance ->
      translationYBy(-distance)
      translationXBy(-distance)
    }
  
    buttonDone.animateInvisible { distance ->
      translationYBy(-distance)
      translationXBy(distance)
    }
    
    palette.animateInvisible { distance ->
      translationXBy(distance)
    }
  
    buttonSwapGradient.animateInvisible { distance ->
      translationXBy(distance)
    }
    
    verticalSeekbar.animateInvisible { distance ->
      translationXBy(-distance)
    }
    
    recyclerBrushes.animateInvisible { distance ->
      translationYBy(distance)
    }
  }
  
  private fun animateVisible() {
    buttonUndo.animateVisible()
    buttonDone.animateVisible()
    palette.animateVisible()
    buttonSwapGradient.animateVisible()
    verticalSeekbar.animateVisible()
    recyclerBrushes.animateVisible()
  }
  
  private fun distanceOf(view: View): Float = when (view) {
    buttonUndo -> buttonUndo.height / 3f
    buttonDone -> buttonDone.height / 3f
    palette -> palette.width / 2f
    buttonSwapGradient -> buttonSwapGradient.width / 2f
    verticalSeekbar -> verticalSeekbar.width / 2f
    recyclerBrushes -> recyclerBrushes.height / 2f
    else -> throwEx()
  }
  
  private fun View.animateInvisible(block: ViewPropertyAnimator.(Float) -> Unit) {
    animate(0f, block)
  }
  
  private fun View.animateVisible() {
    animate(endAlpha = 1f) {
      translationX(0f)
      translationY(0f)
    }
  }
  
  private fun View.animate(endAlpha: Float, block: ViewPropertyAnimator.(Float) -> Unit) {
    val distance = distanceOf(this)
    val animator = animate().apply {
      interpolator = AccelerateDecelerateInterpolator
      duration = DURATION_DEFAULT
    }
    block(animator, distance)
    animator.alpha(endAlpha)
    animator.start()
  }
}