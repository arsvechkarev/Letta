package com.arsvechkarev.letta.features.drawing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.R
import kotlinx.android.synthetic.main.fragment_drawing.backgroundColor
import kotlinx.android.synthetic.main.fragment_drawing.backgroundImage
import kotlinx.android.synthetic.main.fragment_drawing.backgroundImagesRecyclerView
import kotlinx.android.synthetic.main.fragment_drawing.chooseBackgroundDialog
import kotlinx.android.synthetic.main.fragment_drawing.chooseBgGradientPalette
import kotlinx.android.synthetic.main.fragment_drawing.drawingCanvas
import kotlinx.android.synthetic.main.fragment_drawing.imageCurrentBackground
import kotlinx.android.synthetic.main.fragment_drawing.imageUndo
import kotlinx.android.synthetic.main.fragment_drawing.paintDisplayer
import kotlinx.android.synthetic.main.fragment_drawing.palette
import kotlinx.android.synthetic.main.fragment_drawing.verticalSeekbar

class DrawingFragment : Fragment(R.layout.fragment_drawing) {
  
  private lateinit var paintContainer: PaintContainer
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    paintContainer = PaintContainer(imageUndo, drawingCanvas, palette,
      verticalSeekbar, paintDisplayer)
    ChooseBackgroundContainer(
      backgroundImage,
      backgroundColor,
      imageCurrentBackground,
      chooseBgGradientPalette,
      backgroundImagesRecyclerView
    )
    imageCurrentBackground.setOnClickListener {
      chooseBackgroundDialog.show()
    }
  }
}
