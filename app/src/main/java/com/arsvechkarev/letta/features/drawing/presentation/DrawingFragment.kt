package com.arsvechkarev.letta.features.drawing.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.MvpFragment
import com.arsvechkarev.letta.features.drawing.data.ImageUploadingRepository
import kotlinx.android.synthetic.main.fragment_drawing.backgroundColor
import kotlinx.android.synthetic.main.fragment_drawing.backgroundImage
import kotlinx.android.synthetic.main.fragment_drawing.backgroundImagesRecyclerView
import kotlinx.android.synthetic.main.fragment_drawing.chooseBackgroundDialog
import kotlinx.android.synthetic.main.fragment_drawing.chooseBgGradientPalette
import kotlinx.android.synthetic.main.fragment_drawing.drawingView
import kotlinx.android.synthetic.main.fragment_drawing.imageCurrentBackground
import kotlinx.android.synthetic.main.fragment_drawing.imageDone
import kotlinx.android.synthetic.main.fragment_drawing.imageUndo
import kotlinx.android.synthetic.main.fragment_drawing.paintDisplayer
import kotlinx.android.synthetic.main.fragment_drawing.palette
import kotlinx.android.synthetic.main.fragment_drawing.verticalSeekbar

class DrawingFragment : MvpFragment<DrawingMvpView, DrawingPresenter>(
  DrawingPresenter::class, R.layout.fragment_drawing
), DrawingMvpView {
  
  private lateinit var paintContainer: PaintContainer
  private lateinit var chooseBackgroundContainer: ChooseBackgroundContainer
  
  override fun createPresenter(): DrawingPresenter {
    return DrawingPresenter(ImageUploadingRepository(requireContext()))
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    paintContainer = PaintContainer(
      imageUndo, drawingView, palette,
      verticalSeekbar, paintDisplayer)
    chooseBackgroundContainer = ChooseBackgroundContainer(
      backgroundImage,
      backgroundColor,
      imageCurrentBackground,
      chooseBgGradientPalette,
      backgroundImagesRecyclerView
    )
    imageCurrentBackground.setOnClickListener {
      chooseBackgroundDialog.show()
    }
    imageDone.setOnClickListener {
      presenter.uploadBitmap(drawingView, chooseBackgroundContainer.getImageBackground())
    }
  }
  
  override fun onImageStartUploading() {
    Toast.makeText(requireContext(), "Start uploading", Toast.LENGTH_SHORT).show()
  }
  
  override fun onImageUploaded() {
    Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
  }
  
  override fun onImageUploadingError() {
    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
  }
  
  override fun onBackPressed(): Boolean {
    if (chooseBackgroundDialog.isOpened) {
      chooseBackgroundDialog.hide()
      return false
    }
    return true
  }
}
