package com.arsvechkarev.letta.features.drawing.presentation

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.core.DURATION_DEFAULT
import com.arsvechkarev.letta.core.throwEx
import com.arsvechkarev.letta.views.Image
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

class ToolsAnimator(
  private val imageUndo: Image,
  private val imageDone: Image,
  private val palette: GradientPalette,
  private val imageSwapGradient: Image,
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
    imageUndo.animateInvisible { distance ->
      translationYBy(-distance)
      translationXBy(-distance)
    }
    
    imageDone.animateInvisible { distance ->
      translationYBy(-distance)
      translationXBy(distance)
    }
    
    palette.animateInvisible { distance ->
      translationXBy(distance)
    }
    
    imageSwapGradient.animateInvisible { distance ->
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
    imageUndo.animateVisible()
    imageDone.animateVisible()
    palette.animateVisible()
    imageSwapGradient.animateVisible()
    verticalSeekbar.animateVisible()
    recyclerBrushes.animateVisible()
  }
  
  private fun distanceOf(view: View): Float = when (view) {
    imageUndo -> imageUndo.height / 3f
    imageDone -> imageDone.height / 3f
    palette -> palette.width / 2f
    imageSwapGradient -> imageSwapGradient.width / 2f
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
      interpolator = AccelerateDecelerateInterpolator()
      duration = DURATION_DEFAULT
    }
    block(animator, distance)
    animator.alpha(endAlpha)
    animator.start()
  }
}