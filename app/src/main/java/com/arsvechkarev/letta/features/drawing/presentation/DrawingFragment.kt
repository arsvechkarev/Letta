package com.arsvechkarev.letta.features.drawing.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.MvpFragment
import com.arsvechkarev.letta.features.drawing.data.ImageUploadingRepository
import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.brushes.EllipticalBrush
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.opengldrawing.drawing.Renderer
import com.arsvechkarev.letta.opengldrawing.drawing.Size
import kotlinx.android.synthetic.main.fragment_drawing.imageDone
import kotlinx.android.synthetic.main.fragment_drawing.imageUndo
import kotlinx.android.synthetic.main.fragment_drawing.paintDisplayer
import kotlinx.android.synthetic.main.fragment_drawing.paintingViewGroup
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
    val undoStore = UndoStore(onHistoryChanged = {
      paintContainer.onHistoryChanged()
    })
    val openGLDrawingView = createOpenGLDrawingView(undoStore)
    paintContainer = PaintContainer(undoStore, imageUndo,
      openGLDrawingView, palette, verticalSeekbar,
      paintDisplayer
    )
    paintingViewGroup.addDrawingView(openGLDrawingView)
    paintingViewGroup.assignImagesIds(imageUndo.id, imageDone.id)
    imageDone.setOnClickListener {
      val bitmap = openGLDrawingView.getResultBitmap()
      presenter.uploadBitmap(bitmap)
    }
  }
  
  override fun onStartUploadingImage() {
    Toast.makeText(requireContext(), "Start uploading", Toast.LENGTH_SHORT).show()
  }
  
  override fun onImageUploaded() {
    Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
  }
  
  override fun onImageUploadingError() {
    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
  }
  
  override fun onDestroyView() {
    paintContainer.shutdown()
    super.onDestroyView()
  }
  
  private fun createOpenGLDrawingView(undoStore: UndoStore): OpenGLDrawingView {
    val initialBrush = EllipticalBrush()
    val bitmap = getBitmapBy(requireContext(), arguments!!)
    val size = Size(bitmap.width.toFloat(), bitmap.height.toFloat())
    val renderer = object : Renderer {
      override fun shouldDraw() = true
    }
    return OpenGLDrawingView(
      requireContext(), size, initialBrush, undoStore, bitmap, renderer
    )
  }
}
