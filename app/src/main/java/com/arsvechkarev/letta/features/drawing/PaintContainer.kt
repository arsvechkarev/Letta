package com.arsvechkarev.letta.features.drawing

import android.graphics.Color
import com.arsvechkarev.letta.core.brushes.CircleBrush
import com.arsvechkarev.letta.utils.exponentiate
import com.arsvechkarev.letta.views.BrushDisplayer
import com.arsvechkarev.letta.views.DrawingView
import com.arsvechkarev.letta.views.GradientPalette
import com.arsvechkarev.letta.views.SimpleImage
import com.arsvechkarev.letta.views.VerticalSeekbar

class PaintContainer(
  undoImage: SimpleImage,
  drawingView: DrawingView,
  palette: GradientPalette,
  verticalSeekbar: VerticalSeekbar,
  brushDisplayer: BrushDisplayer
) {
  
  init {
    val initialPercentSize = 0.3f
    val initialColor = Color.parseColor("#4000FF")
    drawingView.isClickable = true
    drawingView.currentBrush = CircleBrush(initialColor, initialPercentSize.exponentiate())
    drawingView.brushColor = initialColor
    verticalSeekbar.updatePercent(initialPercentSize)
    drawingView.brushWidth = initialPercentSize.exponentiate()
    verticalSeekbar.updateColorIfAllowed(initialColor)
    
    verticalSeekbar.onUp = {
      brushDisplayer.clear()
    }
    undoImage.setOnClickListener {
      drawingView.undo()
    }
    palette.onColorChanged = {
      verticalSeekbar.updateColorIfAllowed(it)
      drawingView.brushColor = it
    }
    verticalSeekbar.onPercentChanged = {
      val width = it.exponentiate()
      drawingView.brushWidth = width
      brushDisplayer.draw(drawingView.brushColor, width)
    }
  }
}