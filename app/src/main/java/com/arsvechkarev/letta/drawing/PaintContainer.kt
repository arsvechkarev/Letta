package com.arsvechkarev.letta.drawing

import com.arsvechkarev.letta.utils.exponentiate
import com.arsvechkarev.letta.views.DrawingCanvas
import com.arsvechkarev.letta.views.GradientPalette
import com.arsvechkarev.letta.views.PaintDisplayer
import com.arsvechkarev.letta.views.SimpleImage
import com.arsvechkarev.letta.views.VerticalSeekbar

class PaintContainer(
  undoImage: SimpleImage,
  private val drawingCanvas: DrawingCanvas,
  private val palette: GradientPalette,
  private val verticalSeekbar: VerticalSeekbar,
  private val paintDisplayer: PaintDisplayer
) {
  
  init {
    drawingCanvas.isClickable = true
    verticalSeekbar.updatePercent(0.3f)
    drawingCanvas.setPaintWidth(0.3f.exponentiate())
    verticalSeekbar.onPercentChanged = {
      val width = it.exponentiate()
      drawingCanvas.setPaintWidth(width)
      val currentColor = palette.currentColor
      paintDisplayer.draw(currentColor, width)
    }
    palette.onColorChanged = {
      verticalSeekbar.updateColorIfAllowed(it)
      drawingCanvas.setPaintColor(it)
    }
    verticalSeekbar.onUp = { paintDisplayer.clear() }
    undoImage.setOnClickListener { drawingCanvas.undo() }
    palette.post {
      drawingCanvas.setPaintColor(palette.currentColor)
      verticalSeekbar.updateColorIfAllowed(palette.currentColor)
    }
  }
}