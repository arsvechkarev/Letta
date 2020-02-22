package com.arsvechkarev.letta.editing

import android.graphics.Color.WHITE
import android.view.View
import android.view.View.TRANSLATION_X
import android.view.View.TRANSLATION_Y
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.animations.animateFadeIn
import com.arsvechkarev.letta.animations.animateFadeOut
import com.arsvechkarev.letta.utils.animate
import com.arsvechkarev.letta.utils.constraints
import com.arsvechkarev.letta.utils.dmInt
import com.arsvechkarev.letta.utils.exponentiate
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.views.DrawingCanvas
import com.arsvechkarev.letta.views.GradientPalette
import com.arsvechkarev.letta.views.OutlinedImage
import com.arsvechkarev.letta.views.ShowerCircle
import com.arsvechkarev.letta.views.VerticalSeekbar

// TODO: 2/22/2020 Add rainbow to white/black swapper
// TODO: 2/22/2020 Add more colors to palette
class PaintContainer(
  view: View,
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
      undoTool.animateFadeIn()
      undoTool.animate(TRANSLATION_Y, undoTool.height.f * 2, 0f)
      eraserTool.animateFadeIn()
      eraserTool.animate(TRANSLATION_Y, eraserTool.height.f * 2, 0f)
      verticalSeekbar.animateFadeIn()
      verticalSeekbar.animate(TRANSLATION_X, -verticalSeekbar.width / 2f, 0f)
      paletteTool.animateFadeIn()
      paletteTool.animate(TRANSLATION_X, paletteTool.width / 2f, 0f)
    }
  }
  
  override fun animateExit(andThen: () -> Unit) {
    drawingCanvas.isClickable = false
    undoTool.animateFadeOut()
    undoTool.animate(TRANSLATION_Y, undoTool.height.f * 2)
    eraserTool.animateFadeOut()
    eraserTool.animate(TRANSLATION_Y, eraserTool.height.f * 2)
    verticalSeekbar.animateFadeOut()
    verticalSeekbar.animate(TRANSLATION_X, -verticalSeekbar.width / 2f)
    paletteTool.animateFadeOut(andThen)
    paletteTool.animate(TRANSLATION_X, paletteTool.width / 2f, onEnd = {
      andThen()
    })
  }
}