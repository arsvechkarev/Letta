package com.arsvechkarev.letta.features.drawing.presentation

import android.graphics.Color
import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.views.BrushDisplayer
import com.arsvechkarev.letta.views.Image
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette
import kotlin.math.pow

class PaintContainer(
  undoStore: UndoStore,
  undoImage: Image,
  private val openGLDrawingView: OpenGLDrawingView,
  palette: GradientPalette,
  verticalSeekbar: VerticalSeekbar,
  brushDisplayer: BrushDisplayer
) {
  
  init {
    val initialPercentSize = 0.3f
    val initialColor = Color.parseColor("#4000FF")
    verticalSeekbar.updatePercent(initialPercentSize)
  
    openGLDrawingView.updateBrushSize(2f)
    openGLDrawingView.updateColor(Color.RED)
  
    verticalSeekbar.updateColorIfAllowed(initialColor)
  
    verticalSeekbar.onUp = {
      brushDisplayer.clear()
    }
    undoImage.setOnClickListener {
      undoStore.undo()
    }
    palette.onColorChanged = {
      verticalSeekbar.updateColorIfAllowed(it)
      openGLDrawingView.updateColor(it)
    }
    verticalSeekbar.onPercentChanged = {
      val width = it.exponentiate()
      openGLDrawingView.updateBrushSize(width)
      brushDisplayer.draw(openGLDrawingView.currentColor, width)
    }
  }
  
  private fun Float.exponentiate(): Float {
    return this * 50 + (this * 6).pow(3.5f)
  }
  
  fun shutdown() {
    openGLDrawingView.shutdown()
  }
}