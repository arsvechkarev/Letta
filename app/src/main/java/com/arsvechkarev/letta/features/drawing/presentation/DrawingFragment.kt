package com.arsvechkarev.letta.features.drawing.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.MvpFragment
import com.arsvechkarev.letta.core.rotate
import com.arsvechkarev.letta.features.drawing.data.ImageUploadingRepository
import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.brushes.BRUSHES
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.opengldrawing.drawing.Renderer
import com.arsvechkarev.letta.opengldrawing.drawing.Size
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette.Companion.SWAP_ANIMATION_DURATION
import kotlinx.android.synthetic.main.fragment_drawing.imageDone
import kotlinx.android.synthetic.main.fragment_drawing.imageHideTools
import kotlinx.android.synthetic.main.fragment_drawing.imageSwapGradient
import kotlinx.android.synthetic.main.fragment_drawing.imageUndo
import kotlinx.android.synthetic.main.fragment_drawing.paintDisplayer
import kotlinx.android.synthetic.main.fragment_drawing.paintingViewGroup
import kotlinx.android.synthetic.main.fragment_drawing.paletteBrushColor
import kotlinx.android.synthetic.main.fragment_drawing.recyclerBrushes
import kotlinx.android.synthetic.main.fragment_drawing.verticalSeekbar

class DrawingFragment : MvpFragment<DrawingMvpView, DrawingPresenter>(
  DrawingPresenter::class, R.layout.fragment_drawing
), DrawingMvpView {
  
  private lateinit var drawingContainer: DrawingContainer
  
  override fun createPresenter(): DrawingPresenter {
    return DrawingPresenter(ImageUploadingRepository(requireContext()))
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val undoStore = UndoStore(onHistoryChanged = {
      drawingContainer.onHistoryChanged()
    })
    val openGLDrawingView = createOpenGLDrawingView(undoStore)
    drawingContainer = DrawingContainer(
      undoStore, imageUndo, openGLDrawingView, imageDone, paletteBrushColor,
      imageSwapGradient, verticalSeekbar, paintDisplayer, recyclerBrushes
    )
    paintingViewGroup.addDrawingView(openGLDrawingView)
    initClickListeners(openGLDrawingView)
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
    drawingContainer.shutdown()
    super.onDestroyView()
  }
  
  private fun initClickListeners(openGLDrawingView: OpenGLDrawingView) {
    imageDone.setOnClickListener {
      val bitmap = openGLDrawingView.getResultBitmap()
      presenter.uploadBitmap(bitmap)
    }
    imageSwapGradient.setOnClickListener {
      imageSwapGradient.rotate(SWAP_ANIMATION_DURATION)
      paletteBrushColor.swapGradientMode()
    }
    imageHideTools.setOnClickListener {
      drawingContainer.toggleToolsVisibility()
    }
  }
  
  private fun createOpenGLDrawingView(undoStore: UndoStore): OpenGLDrawingView {
    val bitmap = getBitmapBy(requireContext(), arguments!!)
    val size = Size(bitmap.width.toFloat(), bitmap.height.toFloat())
    val renderer = object : Renderer {
      override fun shouldDraw() = true
    }
    return OpenGLDrawingView(
      requireContext(), size, BRUSHES[0], undoStore, bitmap, renderer
    )
  }
}
