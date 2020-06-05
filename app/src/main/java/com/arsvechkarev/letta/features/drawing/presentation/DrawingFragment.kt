package com.arsvechkarev.letta.features.drawing.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.MvpFragment
import com.arsvechkarev.letta.features.drawing.data.ImageUploadingRepository
import kotlinx.android.synthetic.main.fragment_drawing.drawingView
import kotlinx.android.synthetic.main.fragment_drawing.imageDone
import kotlinx.android.synthetic.main.fragment_drawing.imageUndo
import kotlinx.android.synthetic.main.fragment_drawing.paintDisplayer
import kotlinx.android.synthetic.main.fragment_drawing.palette
import kotlinx.android.synthetic.main.fragment_drawing.verticalSeekbar

class DrawingFragment : MvpFragment<DrawingMvpView, DrawingPresenter>(
  DrawingPresenter::class, R.layout.fragment_drawing
), DrawingMvpView {
  
  private lateinit var paintContainer: PaintContainer
  
  override fun createPresenter(): DrawingPresenter {
    return DrawingPresenter(ImageUploadingRepository(requireContext()))
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    paintContainer = PaintContainer(
      imageUndo, drawingView, palette,
      verticalSeekbar, paintDisplayer)
    imageDone.setOnClickListener {
      presenter.uploadBitmap(drawingView)
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
}
