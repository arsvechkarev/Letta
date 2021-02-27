package com.arsvechkarev.letta.features.drawing.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.core.BRUSHES
import com.arsvechkarev.letta.features.drawing.list.BrushAdapter
import com.arsvechkarev.letta.views.BrushDisplayer
import com.arsvechkarev.letta.views.HideToolsView
import com.arsvechkarev.letta.views.ImageButton
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette
import com.arsvechkarev.opengldrawing.Brush
import com.arsvechkarev.opengldrawing.UndoStore
import com.arsvechkarev.opengldrawing.drawing.OpenGLDrawingView
import kotlin.math.pow

class DrawingContainer(
  private val undoStore: UndoStore,
  private val openGLDrawingView: OpenGLDrawingView,
  private val buttonUndo: ImageButton,
  private val buttonDone: ImageButton,
  imageHideTools: HideToolsView,
  palette: GradientPalette,
  buttonSwapGradient: ImageButton,
  verticalSeekbar: VerticalSeekbar,
  brushDisplayer: BrushDisplayer,
  recyclerBrushes: RecyclerView
) {
  
  private val toolsAnimator = ToolsAnimator(
    buttonUndo, buttonDone, palette, buttonSwapGradient,
    verticalSeekbar, recyclerBrushes
  )
  
  private val brushAdapter = BrushAdapter(BRUSHES, onBrushSelected = {
    updateBrush(it)
  })
  
  init {
    val initialBrushPercent = 0.3f
    verticalSeekbar.updatePercent(initialBrushPercent)
    openGLDrawingView.updateBrushSize(initialBrushPercent.exponentiate())
    buttonUndo.isEnabled = false
    buttonDone.isEnabled = false
  
    verticalSeekbar.onUp = { brushDisplayer.clear() }
    buttonUndo.setOnClickListener { undoStore.undo() }
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
    val canUndo = undoStore.isNotEmpty
    buttonUndo.isEnabled = canUndo
    buttonDone.isEnabled = canUndo
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