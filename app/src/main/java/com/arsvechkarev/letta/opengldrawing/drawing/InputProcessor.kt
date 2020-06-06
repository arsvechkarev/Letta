package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.Matrix
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import com.arsvechkarev.letta.core.async.AndroidThreader.onMainThread
import java.util.Vector
import kotlin.math.floor
import kotlin.math.pow

class InputProcessor(private val openGLDrawingView: OpenGLDrawingView) {
  
  private var startedDrawing = false
  private var isFirst = false
  private var hasMoved = false
  private var clearBuffer = false
  private var lastLocation = Point()
  private var lastRemainder = 0.0
  private val points = arrayOfNulls<Point>(3)
  private var pointsCount = 0
  private val invertMatrix = Matrix()
  private val tempPoints = FloatArray(2)
  
  fun setMatrix(matrix: Matrix) {
    matrix.invert(invertMatrix)
  }
  
  fun process(event: MotionEvent) {
    val action = event.actionMasked
    val x = event.x
    val y = openGLDrawingView.height - event.y
    tempPoints[0] = x
    tempPoints[1] = y
    invertMatrix.mapPoints(tempPoints)
    val location = Point(tempPoints[0].toDouble(), tempPoints[1].toDouble())
    when (action) {
      ACTION_DOWN, ACTION_MOVE -> {
        if (!startedDrawing) {
          startedDrawing = true
          hasMoved = false
          isFirst = true
          lastLocation = location
          points[0] = location
          pointsCount = 1
          clearBuffer = true
        } else {
          if (!hasMoved) {
            openGLDrawingView.onBeganDrawing()
            hasMoved = true
          }
          points[pointsCount] = location
          pointsCount++
          if (pointsCount == 3) {
            smoothAndPaintPoints(false)
          }
          lastLocation = location
        }
      }
      ACTION_UP -> {
        if (!hasMoved) {
          if (openGLDrawingView.isDrawAllowed) {
            location.edge = true
            paintPath(Path(arrayOf(location)))
          }
          reset()
        } else if (pointsCount > 0) {
          smoothAndPaintPoints(true)
        }
        pointsCount = 0
        openGLDrawingView.painting.commitStroke(openGLDrawingView.currentColor)
        startedDrawing = false
        openGLDrawingView.onFinishedDrawing(hasMoved)
      }
    }
  }
  
  private fun reset() {
    pointsCount = 0
  }
  
  private fun smoothAndPaintPoints(ended: Boolean) {
    if (pointsCount > 2) {
      val points = Vector<Point>()
      val prev2 = this.points[0]
      val prev1 = this.points[1]
      val cur = this.points[2]
      if (cur == null || prev1 == null || prev2 == null) {
        return
      }
      val midPoint1 = prev1.multiplySum(prev2, 0.5)
      val midPoint2 = cur.multiplySum(prev1, 0.5)
      val segmentDistance = 1
      val distance = midPoint1.distanceTo(midPoint2)
      val numberOfSegments = 48.0.coerceAtMost(
        floor(distance / segmentDistance.toDouble()).coerceAtLeast(24.0)).toInt()
      var t = 0.0f
      val step = 1.0f / numberOfSegments.toFloat()
      for (j in 0 until numberOfSegments) {
        val point = smoothPoint(midPoint1, midPoint2, prev1, t)
        if (isFirst) {
          point.edge = true
          isFirst = false
        }
        points.add(point)
        t += step
      }
      if (ended) {
        midPoint2.edge = true
      }
      points.add(midPoint2)
      val result = arrayOfNulls<Point>(points.size)
      points.toArray(result)
      val path = Path(result)
      paintPath(path)
      System.arraycopy(this.points, 1, this.points, 0, 2)
      pointsCount = if (ended) 0 else 2
    } else {
      val result = arrayOfNulls<Point>(pointsCount)
      System.arraycopy(points, 0, result, 0, pointsCount)
      val path = Path(result)
      paintPath(path)
    }
  }
  
  private fun smoothPoint(midPoint1: Point, midPoint2: Point, prev1: Point, t: Float): Point {
    val a1 = (1.0f - t.toDouble()).pow(2.0)
    val a2 = (2.0f * (1.0f - t) * t).toDouble()
    val a3 = t * t.toDouble()
    return Point(midPoint1.x * a1 + prev1.x * a2 + midPoint2.x * a3,
      midPoint1.y * a1 + prev1.y * a2 + midPoint2.y * a3)
  }
  
  private fun paintPath(path: Path) {
    path.setup(openGLDrawingView.currentColor, openGLDrawingView.currentWeight,
      openGLDrawingView.currentBrush)
    if (clearBuffer) {
      lastRemainder = 0.0
    }
    path.remainder = lastRemainder
    openGLDrawingView.painting.paintStroke(path, clearBuffer) {
      onMainThread {
        lastRemainder = path.remainder
        clearBuffer = false
      }
    }
  }
}