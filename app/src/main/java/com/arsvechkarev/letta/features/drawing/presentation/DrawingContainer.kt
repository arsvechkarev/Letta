package com.arsvechkarev.letta.features.drawing.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.features.drawing.list.BrushAdapter
import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.brushes.BRUSHES
import com.arsvechkarev.letta.opengldrawing.brushes.Brush
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.views.BrushDisplayer
import com.arsvechkarev.letta.views.Image
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette
import kotlin.math.pow

class DrawingContainer(
  private val undoStore: UndoStore,
  private val undoImage: Image,
  private val openGLDrawingView: OpenGLDrawingView,
  palette: GradientPalette,
  verticalSeekbar: VerticalSeekbar,
  brushDisplayer: BrushDisplayer,
  recyclerBrushes: RecyclerView
) {
  
  
  private val brushAdapter = BrushAdapter(BRUSHES, onBrushSelected = {
    updateBrush(it)
  })
  
  init {
    val initialBrushPercent = 0.3f
    verticalSeekbar.updatePercent(initialBrushPercent)
    openGLDrawingView.updateBrushSize(initialBrushPercent.exponentiate())
    undoImage.isEnabled = false
    
    verticalSeekbar.onUp = { brushDisplayer.clear() }
    undoImage.setOnClickListener { undoStore.undo() }
    palette.onColorChanged = { color ->
      verticalSeekbar.updateColorIfAllowed(color)
      openGLDrawingView.updateColor(color)
    }
    verticalSeekbar.onPercentChanged = { percent ->
      val size = percent.exponentiate()
      openGLDrawingView.updateBrushSize(size)
      brushDisplayer.draw(openGLDrawingView.currentColor, size)
    }
    recyclerBrushes.adapter = brushAdapter
    recyclerBrushes.layoutManager = LinearLayoutManager(openGLDrawingView.context,
      LinearLayoutManager.HORIZONTAL, false)
  }
  
  fun onHistoryChanged() {
    val canUndo = undoStore.canUndo
    undoImage.isEnabled = canUndo
  }
  
  fun shutdown() {
    openGLDrawingView.shutdown()
  }
  
  private fun updateBrush(brush: Brush) {
    openGLDrawingView.updateBrush(brush)
  }
  
  private fun Float.exponentiate(): Float {
    return this * 25 + (this * 3.5f).pow(4f)
  }
}