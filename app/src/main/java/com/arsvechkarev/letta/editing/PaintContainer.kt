package com.arsvechkarev.letta.editing

import android.graphics.Color.WHITE
import android.view.View
import android.view.View.TRANSLATION_X
import android.view.View.TRANSLATION_Y
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.animations.fadeIn
import com.arsvechkarev.letta.animations.fadeOut
import com.arsvechkarev.letta.utils.animate
import com.arsvechkarev.letta.utils.constraints
import com.arsvechkarev.letta.utils.dmInt
import com.arsvechkarev.letta.utils.exponentiate
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.views.DrawingCanvas
import com.arsvechkarev.letta.views.GradientPalette
import com.arsvechkarev.letta.views.ListenableConstraintLayout
import com.arsvechkarev.letta.views.OutlinedImage
import com.arsvechkarev.letta.views.ShowerCircle
import com.arsvechkarev.letta.views.VerticalSeekbar

class PaintContainer(
  view: ListenableConstraintLayout,
  private val drawingCanvas: DrawingCanvas,
  private val topControlView: View
) : Container(view) {
  
  private var paletteTool: GradientPalette = findViewById(R.id.palette)
  private var verticalSeekbar: VerticalSeekbar = findViewById(R.id.verticalSeekbar)
  private var undoTool: OutlinedImage = findViewById(R.id.imageUndo)
  private var eraserTool: OutlinedImage = findViewById(R.id.imageEraser)
  private var showerCircle: ShowerCircle = findViewById(R.id.showCircle)
  
  init {
    post {
      drawingCanvas.setPaintColor(paletteTool.currentColor)
      verticalSeekbar.updateColorIfAllowed(paletteTool.currentColor)
      verticalSeekbar.updatePercent(0.3f)
      drawingCanvas.setPaintWidth(0.3f.exponentiate())
      verticalSeekbar.onPercentChanged = {
        val width = it.exponentiate()
        drawingCanvas.setPaintWidth(width)
        val currentColor = if (drawingCanvas.isEraserMode) WHITE else paletteTool.currentColor
        showerCircle.draw(currentColor, width)
      }
      paletteTool.onColorChanged = {
        if (drawingCanvas.isEraserMode) {
          drawingCanvas.isEraserMode = false
          eraserTool.inverse()
        }
        verticalSeekbar.updateColorIfAllowed(it)
        drawingCanvas.setPaintColor(it)
      }
      verticalSeekbar.onUp = { showerCircle.clear() }
      undoTool.setOnClickListener { drawingCanvas.undo() }
      eraserTool.setOnClickListener {
        drawingCanvas.isEraserMode = !drawingCanvas.isEraserMode
      }
      undoTool.constraints {
        topMargin = topControlView.height + dmInt(R.dimen.img_tool_m_top2) * 2
      }
    }
  }
  
  override fun animateEnter() {
    post {
      drawingCanvas.isClickable = true
      undoTool.fadeIn()
      undoTool.animate(TRANSLATION_Y, undoTool.height.f * 2, 0f)
      eraserTool.fadeIn()
      eraserTool.animate(TRANSLATION_Y, eraserTool.height.f * 2, 0f)
      verticalSeekbar.fadeIn()
      verticalSeekbar.animate(TRANSLATION_X, -verticalSeekbar.width / 2f, 0f)
      paletteTool.fadeIn()
      paletteTool.animate(TRANSLATION_X, paletteTool.width / 2f, 0f)
    }
  }
  
  override fun animateExit(andThen: () -> Unit) {
    drawingCanvas.isClickable = false
    undoTool.fadeOut()
    undoTool.animate(TRANSLATION_Y, undoTool.height.f * 2)
    eraserTool.fadeOut()
    eraserTool.animate(TRANSLATION_Y, eraserTool.height.f * 2)
    verticalSeekbar.fadeOut()
    verticalSeekbar.animate(TRANSLATION_X, -verticalSeekbar.width / 2f)
    paletteTool.fadeOut(andThen)
    paletteTool.animate(TRANSLATION_X, paletteTool.width / 2f, onEnd = {
      andThen()
    })
  }
}