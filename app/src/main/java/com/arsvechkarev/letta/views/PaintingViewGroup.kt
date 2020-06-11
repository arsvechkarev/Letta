package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.layoutNormal
import com.arsvechkarev.letta.core.layoutWithLeftTop
import com.arsvechkarev.letta.core.withParams
import com.arsvechkarev.letta.extensions.findChild
import com.arsvechkarev.letta.extensions.totalHeight
import com.arsvechkarev.letta.extensions.totalWidth
import com.arsvechkarev.letta.opengldrawing.drawing.OpenGLDrawingView
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

class PaintingViewGroup @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
  
  fun addDrawingView(drawingView: OpenGLDrawingView) {
    addView(drawingView, 0)
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
    val heightSize = MeasureSpec.getSize(heightMeasureSpec)
  
    childWithClass<RecyclerView>().measure(widthMeasureSpec,
      MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST))
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val height = b - t
    val width = r - l
    val imageDone = childWithId(R.id.imageDone)
    val imageUndo = childWithId(R.id.imageUndo)
    val imageSwapGradient = childWithId(R.id.imageSwapGradient)
    val recyclerBrushes = childWithClass<RecyclerView>()
  
    childWithClass<OpenGLDrawingView>().layout(0, 0, width, height)
  
    childWithClass<BrushDisplayer>().layout(0, 0, width, height)
  
    val imageDoneOffset: Int
    imageDone.withParams { params ->
      imageDoneOffset = maxOf(params.width, params.height) / 5
      layoutNormal(
        left = width - params.width - imageDoneOffset,
        top = imageDoneOffset,
        right = width - imageDoneOffset,
        bottom = imageDoneOffset + params.height
      )
    }
  
    imageUndo.withParams { params ->
      val imageUndoOffset = maxOf(params.width, params.height) / 4
      layoutNormal(
        left = imageUndoOffset,
        top = imageUndoOffset,
        right = imageUndoOffset + params.width,
        bottom = imageUndoOffset + params.height
      )
    }
  
    childWithClass<VerticalSeekbar>().withParams { params ->
      layoutNormal(
        left = 0,
        top = height / 2 - params.height / 2,
        right = params.width,
        bottom = height / 2 + params.height / 2
      )
    }
  
    val gradientPalette = childWithClass<GradientPalette>()
  
    // Group refers to gradient palette and image that swaps gradient mode
    val halfGroupWidth = maxOf(gradientPalette.totalWidth, imageSwapGradient.totalWidth) / 2
    val halfGroupHeight = (gradientPalette.totalHeight + imageSwapGradient.totalHeight) / 2
  
    gradientPalette.withParams { params ->
      val halfPaletteWidth = params.width / 2
      val left = width - halfGroupWidth - halfPaletteWidth
      val top = height / 2 - halfGroupHeight
      layoutNormal(
        left = left,
        top = top,
        right = left + params.width,
        bottom = top + params.height
      )
    }
  
    imageSwapGradient.withParams { params ->
      layoutWithLeftTop(
        left = width - halfGroupWidth - params.width / 2,
        top = gradientPalette.bottom + params.height / 3,
        params = params
      )
    }
  
    recyclerBrushes.withParams {
      layoutNormal(
        left = 0,
        top = height - measuredHeight,
        right = width,
        bottom = height
      )
    }
  
    childWithClass<HideToolsView>().withParams { params ->
      val middleY = imageDone.bottom + (gradientPalette.top - imageDone.bottom) / 2
      layoutNormal(
        left = width - params.width - imageDoneOffset,
        top = middleY - params.height / 2,
        right = width - imageDoneOffset,
        bottom = middleY + params.height / 2
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