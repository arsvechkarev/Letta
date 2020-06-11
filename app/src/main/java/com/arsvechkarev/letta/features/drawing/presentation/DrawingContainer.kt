package com.arsvechkarev.letta.features.drawing.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.features.drawing.list.BrushAdapter
import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.brushes.BRUSHES
import com.arsvechkarev.letta.opengldrawing.brushes.Brush
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.views.BrushDisplayer
import com.arsvechkarev.letta.views.HideToolsView
import com.arsvechkarev.letta.views.Image
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette
import kotlin.math.pow

class DrawingContainer(
  private val undoStore: UndoStore,
  private val openGLDrawingView: OpenGLDrawingView,
  private val imageUndo: Image,
  imageDone: Image,
  imageHideTools: HideToolsView,
  palette: GradientPalette,
  imageSwapGradient: Image,
  verticalSeekbar: VerticalSeekbar,
  brushDisplayer: BrushDisplayer,
  recyclerBrushes: RecyclerView
) {
  
  private val toolsAnimator = ToolsAnimator(
    imageUndo, imageDone, palette, imageSwapGradient,
    verticalSeekbar, recyclerBrushes
  )
  
  private val brushAdapter = BrushAdapter(BRUSHES, onBrushSelected = {
    updateBrush(it)
  })
  
  init {
    val initialBrushPercent = 0.3f
    verticalSeekbar.updatePercent(initialBrushPercent)
    openGLDrawingView.updateBrushSize(initialBrushPercent.exponentiate())
    imageUndo.isEnabled = false
    
    verticalSeekbar.onUp = { brushDisplayer.clear() }
    imageUndo.setOnClickListener { undoStore.undo() }
    palette.onColorChanged = { color ->
      verticalSeekbar.updateColorIfAllowed(color)
      openGLDrawingView.updateColor(color)
    }
    verticalSeekbar.onPercentChanged = { percent ->
      val size = percent.exponentiate()
      openGLDrawingView.updateBrushSize(size)
      brushDisplayer.draw(openGLDrawingView.currentColor, size)
    }
    openGLDrawingView.onDown = {
      if (!toolsAnimator.toolsAreVisible) imageHideTools.makeInvisible()
    }
    openGLDrawingView.onUp = {
      if (!toolsAnimator.toolsAreVisible) imageHideTools.makeVisible()
    }
    recyclerBrushes.adapter = brushAdapter
    recyclerBrushes.layoutManager = LinearLayoutManager(openGLDrawingView.context,
      LinearLayoutManager.HORIZONTAL, false)
  }
  
  fun toggleToolsVisibility() {
    toolsAnimator.toggleVisibility()
  }
  
  fun onHistoryChanged() {
    val canUndo = undoStore.canUndo
    imageUndo.isEnabled = canUndo
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