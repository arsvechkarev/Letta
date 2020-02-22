package com.arsvechkarev.letta.editing

import android.view.View
import android.view.View.TRANSLATION_X
import android.view.View.TRANSLATION_Y
import android.widget.ImageButton
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
import com.arsvechkarev.letta.views.ShowerCircle
import com.arsvechkarev.letta.views.VerticalSeekbar

// TODO: 2/22/2020 Add eraser
// TODO: 2/22/2020 Add rainbow to white/black swapper
// TODO: 2/22/2020 Add more colors to palette
class PaintContainer(
  view: View,
  private val drawingCanvas: DrawingCanvas,
  private val topControlView: View
) : Container(view) {
  
  private var palette: GradientPalette = findViewById(R.id.palette)
  private var verticalSeekbar: VerticalSeekbar = findViewById(R.id.verticalSeekbar)
  private var buttonBack: ImageButton = findViewById(R.id.buttonUndo)
  private var buttonEraser: ImageButton = findViewById(R.id.buttonEraser)
  private var showerCircle: ShowerCircle = findViewById(R.id.showCircle)
  
  init {
    post {
      drawingCanvas.setPaintColor(palette.currentColor)
      verticalSeekbar.updatePercent(0.3f)
      drawingCanvas.setPaintWidth(0.3f.exponentiate())
      verticalSeekbar.onPercentChanged = {
        val width = it.exponentiate()
        drawingCanvas.setPaintWidth(width)
        showerCircle.draw(palette.currentColor, width)
      }
      verticalSeekbar.onUp = { showerCircle.erase() }
      palette.onColorChanged = { drawingCanvas.setPaintColor(it) }
      buttonBack.setOnClickListener { drawingCanvas.undo() }
      buttonBack.constraints {
        topMargin = topControlView.height + dmInt(R.dimen.img_tool_m_top2) * 2
      }
    }
  }
  
  override fun animateEnter() {
    post {
      drawingCanvas.isClickable = true
      buttonBack.animateFadeIn()
      buttonBack.animate(TRANSLATION_Y, buttonBack.height.f, 0f)
      buttonEraser.animateFadeIn()
      buttonEraser.animate(TRANSLATION_Y, buttonEraser.height.f, 0f)
      verticalSeekbar.animateFadeIn()
      verticalSeekbar.animate(TRANSLATION_X, -verticalSeekbar.width / 2f, 0f)
      palette.animateFadeIn()
      palette.animate(TRANSLATION_X, palette.width / 2f, 0f)
    }
  }
  
  override fun animateExit(andThen: () -> Unit) {
    drawingCanvas.isClickable = false
    buttonBack.animateFadeOut()
    buttonBack.animate(TRANSLATION_Y, buttonBack.height.f)
    buttonEraser.animateFadeOut()
    buttonEraser.animate(TRANSLATION_Y, buttonEraser.height.f)
    verticalSeekbar.animateFadeOut()
    verticalSeekbar.animate(TRANSLATION_X, -verticalSeekbar.width / 2f)
    palette.animateFadeOut(andThen)
    palette.animate(TRANSLATION_X, palette.width / 2f, onEnd = {
      andThen()
    })
  }
}