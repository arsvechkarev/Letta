package com.arsvechkarev.letta.editing


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.utils.Group
import kotlinx.android.synthetic.main.fragment_edit.buttonDone
import kotlinx.android.synthetic.main.fragment_edit.buttonStickers
import kotlinx.android.synthetic.main.fragment_edit.buttonText
import kotlinx.android.synthetic.main.fragment_edit.buttonUndo
import kotlinx.android.synthetic.main.fragment_edit.drawingCanvas
import kotlinx.android.synthetic.main.fragment_edit.palette
import kotlinx.android.synthetic.main.fragment_edit.seekbar

class EditFragment : Fragment(R.layout.fragment_edit) {
  
  companion object {
    const val FADE_OUT_DURATION = 200L
  }
  
  private lateinit var tools: Group
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    tools = Group(palette, seekbar, buttonUndo, buttonStickers, buttonText, buttonDone)
    palette.onColorSelected { drawingCanvas.setPaintColor(it) }
    seekbar.onPercentChanged { drawingCanvas.changeWidth(it * 30) }
    buttonUndo.setOnClickListener { drawingCanvas.undo() }
    drawingCanvas.onDown = { animateTools(visible = false) }
    drawingCanvas.onUp = { animateTools(visible = true) }
  }
  
  private fun animateTools(visible: Boolean) {
    tools.animateAlpha(visible, FADE_OUT_DURATION)
  }
  
}
