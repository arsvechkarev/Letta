package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.View
import com.arsvechkarev.letta.R

class ViewPort @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  private val layers: MutableList<Layer> = ArrayList()
  private var target: Layer? = null
  
  init {
    setBackgroundColor(Color.BLACK)
  }
  
  @Suppress("DEPRECATION")
  fun addText(text: CharSequence, paint: TextPaint) {
    val layout = StaticLayout(text, paint, paint.measureText(text.toString()).toInt(),
      Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true)
    val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    layout.draw(canvas)
    layers.add(Layer(this, bitmap))
    invalidate()
  }
  
  override fun onDraw(canvas: Canvas) {
    for (l in layers) {
      l.draw(canvas)
    }
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == ACTION_DOWN) {
      target = null
      for (i in layers.indices.reversed()) {
        val layer = layers[i]
        if (event in layer) {
          target = layer
          layers.remove(layer)
          layers.add(layer)
          invalidate()
          break
        }
      }
    }
    return if (target == null) {
      false
    } else target!!.onTouchEvent(event)
  }
}

class Layer(private val parentView: View, var bitmap: Bitmap) {
  private val matrix = Matrix()
  private val inverse = Matrix()
  private val bounds = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
  private var matrixGestureDetector = MatrixGestureDetector(matrix) {
    parentView.invalidate()
  }
  
  operator fun contains(event: MotionEvent): Boolean {
    matrix.invert(inverse)
    val pts = floatArrayOf(event.x, event.y)
    inverse.mapPoints(pts)
    return if (!bounds.contains(pts[0], pts[1])) {
      false
    } else Color.alpha(bitmap.getPixel(pts[0].toInt(), pts[1].toInt())) != 0
  }
  
  fun onTouchEvent(event: MotionEvent): Boolean {
    return matrixGestureDetector.onTouchEvent(event)
  }
  
  fun draw(canvas: Canvas) {
    canvas.drawBitmap(bitmap, matrix, null)
  }
}

class MatrixGestureDetector(
  private val matrix: Matrix,
  private val onMatrixChanged: (Matrix) -> Unit
) {
  private val tempMatrix = Matrix()
  private val srcArr = FloatArray(4)
  private val dstArr = FloatArray(4)
  
  fun onTouchEvent(event: MotionEvent): Boolean {
    val index = event.actionIndex

    if (event.pointerCount > 2) return true
    when (event.actionMasked) {
      ACTION_DOWN, ACTION_POINTER_DOWN -> {
        val indexX2 = index * 2
        srcArr[indexX2] = event.getX(index)
        srcArr[indexX2 + 1] = event.getY(index)
        return true
      }
      ACTION_MOVE -> {
        val pointerId = event.getPointerId(index)
        val pointerIdX2 = pointerId * 2
        val maxEvents = event.pointerCount
        for (i in 0 until maxEvents) {
          val idx = pointerIdX2 + i * 2
          dstArr[idx] = event.getX(i)
          dstArr[idx + 1] = event.getY(i)
        }
        tempMatrix.setPolyToPoly(srcArr, pointerIdX2, dstArr, pointerIdX2, maxEvents)
        matrix.postConcat(tempMatrix)
        onMatrixChanged(matrix)
        System.arraycopy(dstArr, 0, srcArr, 0, dstArr.size)
        return true
      }
    }
    return false
  }
  
}