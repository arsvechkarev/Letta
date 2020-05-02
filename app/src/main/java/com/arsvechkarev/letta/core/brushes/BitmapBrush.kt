package com.arsvechkarev.letta.core.brushes

import com.arsvechkarev.letta.utils.i
import kotlin.math.sqrt

abstract class BitmapBrush(
  color: Int,
  brushSize: Float,
  protected var thresholdPx: Float
) : Brush(color, brushSize) {
  
  protected val points = ArrayList<Float>()
  
  override fun onDown(x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
  
  override fun onMove(lastX: Float, lastY: Float, x: Float, y: Float) {
    val dx = x - lastX
    val dy = y - lastY
    val distance = sqrt(dx * dx + dy * dy).i
    if (distance > thresholdPx) {
      var mutableX = lastX
      var mutableY = lastY
      val dxStep = dx / distance
      val dyStep = dy / distance
      repeat(distance) {
        points.add(mutableX)
        points.add(mutableY)
        mutableX += dxStep
        mutableY += dyStep
      }
    } else {
      points.add(x)
      points.add(y)
    }
  }
  
  override fun onUp(x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
}