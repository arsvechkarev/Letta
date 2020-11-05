package com.arsvechkarev.letta.features.drawing.presentation

import android.os.Bundle
import android.view.View
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.mvp.MvpFragment
import com.arsvechkarev.letta.core.navigation.navigator
import com.arsvechkarev.letta.extensions.StatusBar
import com.arsvechkarev.letta.extensions.rotate
import com.arsvechkarev.letta.features.common.CommonDi
import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.brushes.BRUSHES
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.opengldrawing.drawing.Size
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette.Companion.SWAP_ANIMATION_DURATION
import kotlinx.android.synthetic.main.fragment_drawing.brushDisplayer
import kotlinx.android.synthetic.main.fragment_drawing.dialogDiscardChanges
import kotlinx.android.synthetic.main.fragment_drawing.dialogLoading
import kotlinx.android.synthetic.main.fragment_drawing.imageDone
import kotlinx.android.synthetic.main.fragment_drawing.imageHideTools
import kotlinx.android.synthetic.main.fragment_drawing.imageSwapGradient
import kotlinx.android.synthetic.main.fragment_drawing.imageUndo
import kotlinx.android.synthetic.main.fragment_drawing.paintingViewGroup
import kotlinx.android.synthetic.main.fragment_drawing.paletteBrushColor
import kotlinx.android.synthetic.main.fragment_drawing.recyclerBrushes
import kotlinx.android.synthetic.main.fragment_drawing.textDialogCancel
import kotlinx.android.synthetic.main.fragment_drawing.textDialogDiscard
import kotlinx.android.synthetic.main.fragment_drawing.verticalSeekbar

class DrawingFragment : MvpFragment<DrawingMvpView, DrawingPresenter>(
  DrawingPresenter::class, R.layout.fragment_drawing
), DrawingMvpView {
  
  private lateinit var undoStore: UndoStore
  private lateinit var drawingContainer: DrawingContainer
  
  override fun createPresenter(): DrawingPresenter {
    return DrawingPresenter(CommonDi.provideProjectRepository())
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    StatusBar.clearLightStatusBar(requireActivity())
    StatusBar.setStatusBarColor(requireActivity(), Colors.StatusBar)
    undoStore = UndoStore(onHistoryChanged = {
      drawingContainer.onHistoryChanged()
    })
    val openGLDrawingView = createOpenGLDrawingView(undoStore)
    drawingContainer = DrawingContainer(
      undoStore, openGLDrawingView, imageUndo, imageDone,
      imageHideTools, paletteBrushColor, imageSwapGradient,
      verticalSeekbar, brushDisplayer, recyclerBrushes
    )
    paintingViewGroup.addDrawingView(openGLDrawingView)
    initClickListeners(openGLDrawingView)
  }
  
  override fun onStartUploadingImage() {
    dialogLoading.show()
  }
  
  override fun onImageUploaded() {
    dialogLoading.hide()
    navigator.popBackStack()
  }
  
  override fun onDestroyView() {
    drawingContainer.shutdown()
    StatusBar.setLightStatusBar(requireActivity())
    super.onDestroyView()
  }
  
  override fun onBackPressed(): Boolean {
    if (undoStore.isEmpty) {
      return true
    }
    dialogDiscardChanges.show()
    return false
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
    imageHideTools.setOnClickListener { drawingContainer.toggleToolsVisibility() }
    textDialogCancel.setOnClickListener { dialogDiscardChanges.hide() }
    textDialogDiscard.setOnClickListener { navigator.popBackStack() }
  }
  
  private fun createOpenGLDrawingView(undoStore: UndoStore): OpenGLDrawingView {
    val bitmap = getBitmapBy(requireContext(), arguments!!)
    val size = Size(bitmap.width.toFloat(), bitmap.height.toFloat())
    return OpenGLDrawingView(
      requireContext(), size, BRUSHES[0], undoStore, bitmap
    )
  }
}
