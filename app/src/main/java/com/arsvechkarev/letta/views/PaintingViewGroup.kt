package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.extensions.doLayout
import com.arsvechkarev.letta.extensions.findChild
import com.arsvechkarev.letta.extensions.marginParams
import com.arsvechkarev.letta.extensions.widthWithMargins
import com.arsvechkarev.letta.extensions.withLayoutParams
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

class PaintingViewGroup @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
  
  private var imageUndoId = 0
  private var imageDoneId = 0
  
  fun addDrawingView(drawingView: OpenGLDrawingView) {
    addView(drawingView, 0)
  }
  
  fun assignImagesIds(imageUndoId: Int, imageDoneId: Int) {
    this.imageUndoId = imageUndoId
    this.imageDoneId = imageDoneId
  }
  
  override fun checkLayoutParams(p: LayoutParams): Boolean {
    return p is MarginLayoutParams
  }
  
  override fun generateDefaultLayoutParams(): LayoutParams {
    return MarginLayoutParams(MATCH_PARENT, MATCH_PARENT)
  }
  
  override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
    return MarginLayoutParams(context, attrs)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    // Recycler view is the only one who needs to be measured
    val heightSize = MeasureSpec.getSize(heightMeasureSpec)
    childWithClass<RecyclerView>().measure(widthMeasureSpec,
      MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST))
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val height = b - t
    val imageDone = childWithId(imageDoneId)
    val imageUndo = childWithId(imageUndoId)
    val recyclerBrushes = childWithClass<RecyclerView>()
    val recyclerHeight = recyclerBrushes.measuredHeight
  
    childWithClass<OpenGLDrawingView>().layout(0, 0, r, b - recyclerHeight)
  
    childWithClass<BrushDisplayer>().layout(0, 0, r, b - recyclerHeight)
  
    imageDone.withLayoutParams { params ->
      doLayout(
        left = r - params.width - params.rightMargin,
        top = params.topMargin,
        right = r - params.rightMargin,
        bottom = params.height + params.topMargin
      )
    }
  
    imageUndo.withLayoutParams { params ->
      val left = r - imageDone.widthWithMargins - params.marginEnd - params.width
      val top = imageDone.height / 2 - params.height / 2 + imageDone.marginParams.topMargin
      doLayout(
        left = left,
        top = top,
        right = left + params.width,
        bottom = top + params.height
      )
    }
    
    childWithClass<VerticalSeekbar>().withLayoutParams { params ->
      doLayout(
        left = 0,
        top = height / 2 - params.height / 2,
        right = params.width,
        bottom = height / 2 + params.height / 2
      )
    }
    
    childWithClass<GradientPalette>().withLayoutParams { params ->
      val horizontalOffset = params.width / 2
      doLayout(
        left = r - params.width - horizontalOffset,
        top = height / 2 - params.height / 2,
        right = r - horizontalOffset,
        bottom = height / 2 + params.height / 2
      )
    }
  
    recyclerBrushes.withLayoutParams {
      doLayout(
        left = 0,
        top = b - measuredHeight,
        right = r,
        bottom = b
      )
    }
  }
  
  private fun childWithId(@IdRes id: Int): View {
    return findChild { child -> child.id == id }
  }
  
  private inline fun <reified T : View> childWithClass(): T {
    return findChild { child -> child is T } as T
  }
}