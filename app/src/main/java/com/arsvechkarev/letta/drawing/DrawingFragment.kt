package com.arsvechkarev.letta.drawing


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.R
import kotlinx.android.synthetic.main.fragment_drawing.drawingCanvas
import kotlinx.android.synthetic.main.fragment_drawing.imageUndo
import kotlinx.android.synthetic.main.fragment_drawing.paintDisplayer
import kotlinx.android.synthetic.main.fragment_drawing.palette
import kotlinx.android.synthetic.main.fragment_drawing.verticalSeekbar

class DrawingFragment : Fragment(
  R.layout.fragment_drawing) {
  
  private lateinit var paintContainer: PaintContainer
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    paintContainer = PaintContainer(imageUndo, drawingCanvas, palette,
      verticalSeekbar, paintDisplayer)
  }
}
