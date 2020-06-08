package com.arsvechkarev.letta.features.drawing.presentation

import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.views.BrushDisplayer
import com.arsvechkarev.letta.views.Image
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette
import kotlin.math.pow

class PaintContainer(
  private val undoStore: UndoStore,
  private val undoImage: Image,
  private val openGLDrawingView: OpenGLDrawingView,
  palette: GradientPalette,
  verticalSeekbar: VerticalSeekbar,
  brushDisplayer: BrushDisplayer
) {
  
  init {
    val initialBrushPercent = 0.3f
    verticalSeekbar.updatePercent(initialBrushPercent)
    openGLDrawingView.updateBrushSize(initialBrushPercent.exponentiate())
    verticalSeekbar.onUp = {
      brushDisplayer.clear()
    }
    undoImage.setOnClickListener {
      undoStore.undo()
    }
    undoImage.isEnabled = false
    palette.onColorChanged = {
      verticalSeekbar.updateColorIfAllowed(it)
      openGLDrawingView.updateColor(it)
    }
    verticalSeekbar.onPercentChanged = {
      val size = it.exponentiate()
      openGLDrawingView.updateBrushSize(size)
      brushDisplayer.draw(openGLDrawingView.currentColor, size)
    }
  }
  
  private fun Float.exponentiate(): Float {
    return this * 20 + (this * 4).pow(3.7f)
  }
  
  fun shutdown() {
    openGLDrawingView.shutdown()
  }
  
  fun onHistoryChanged() {
    val canUndo = undoStore.canUndo
    undoImage.isEnabled = canUndo
  }
}