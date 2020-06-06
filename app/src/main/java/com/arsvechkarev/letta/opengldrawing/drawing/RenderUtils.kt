package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.Matrix
import android.graphics.RectF
import android.opengl.GLES20
import com.arsvechkarev.letta.extensions.roundToInts
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

object RenderUtils {
  
  fun renderPath(path: Path, state: RenderState): RectF? {
    state.baseWeight = path.baseWeight
    state.spacing = path.brush.spacing
    state.alpha = path.brush.alpha
    state.angle = path.brush.angle
    state.scale = path.brush.scale
    val length = path.length
    if (length == 0) {
      return null
    }
    if (length == 1) {
      paintStamp(path.points[0]!!, state)
    } else {
      val points = path.points
      state.prepare()
      for (i in 0 until points.size - 1) {
        paintSegment(points[i], points[i + 1], state)
      }
    }
    path.remainder = state.remainder
    return draw(state)
  }
  
  private fun paintSegment(lastPoint: Point?, point: Point?, state: RenderState) {
    val distance = lastPoint!!.distanceTo(point!!).toDouble()
    val vector = point.subtract(lastPoint)
    var unitVector = Point(1.0, 1.0)
    val vectorAngle = if (abs(state.angle) > 0.0f) {
      state.angle
    } else {
      atan2(vector.y, vector.x).toFloat()
    }
    val brushWeight = state.baseWeight * state.scale
    val step = max(1.0f, state.spacing * brushWeight).toDouble()
    if (distance > 0.0) {
      unitVector = vector.multiplyByScalar(1.0 / distance)
    }
    val boldAlpha = min(1.0f, state.alpha * 1.15f)
    var boldHead = lastPoint.edge
    val boldTail = point.edge
    val count = ceil((distance - state.remainder) / step).toInt()
    val currentCount = state.count
    state.appendValuesCount(count)
    state.setPosition(currentCount)
    var start = lastPoint.add(unitVector.multiplyByScalar(state.remainder))
    var succeed = true
    var f = state.remainder
    while (f <= distance) {
      val alpha = if (boldHead) boldAlpha else state.alpha
      succeed = state.addPoint(start.toPointF(), brushWeight, vectorAngle, alpha, -1)
      if (!succeed) {
        break
      }
      start = start.add(unitVector.multiplyByScalar(step))
      boldHead = false
      f += step
    }
    if (succeed && boldTail) {
      state.appendValuesCount(1)
      state.addPoint(point.toPointF(), brushWeight, vectorAngle, boldAlpha, -1)
    }
    state.remainder = f - distance
  }
  
  private fun paintStamp(point: Point, state: RenderState) {
    val brushWeight = state.baseWeight * state.scale
    val start = point.toPointF()
    val angle = if (abs(state.angle) > 0.0f) state.angle else 0.0f
    val alpha = state.alpha
    state.prepare()
    state.appendValuesCount(1)
    state.addPoint(start, brushWeight, angle, alpha, 0)
  }
  
  private fun draw(state: RenderState): RectF {
    val dataBounds = RectF()
    val count = state.count
    if (count == 0) {
      return dataBounds
    }
    val vertexDataSize = 5 * java.lang.Float.SIZE / 8
    val capacity = vertexDataSize * (count * 4 + (count - 1) * 2)
    val bb = ByteBuffer.allocateDirect(capacity)
    bb.order(ByteOrder.nativeOrder())
    val vertexData = bb.asFloatBuffer()
    vertexData.position(0)
    state.setPosition(0)
    var n = 0
    for (i in 0 until count) {
      val x = state.read()
      val y = state.read()
      val size = state.read()
      val angle = state.read()
      val alpha = state.read()
      val rect = RectF(x - size, y - size, x + size, y + size)
      val points = floatArrayOf(
        rect.left, rect.top,
        rect.right, rect.top,
        rect.left, rect.bottom,
        rect.right, rect.bottom
      )
      val centerX = rect.centerX()
      val centerY = rect.centerY()
      val t = Matrix()
      t.setRotate(Math.toDegrees(angle.toDouble()).toFloat(), centerX, centerY)
      t.mapPoints(points)
      t.mapRect(rect)
      rect.roundToInts()
      dataBounds.union(rect)
      if (n != 0) {
        vertexData.put(points[0])
        vertexData.put(points[1])
        vertexData.put(0f)
        vertexData.put(0f)
        vertexData.put(alpha)
        n++
      }
      vertexData.put(points[0])
      vertexData.put(points[1])
      vertexData.put(0f)
      vertexData.put(0f)
      vertexData.put(alpha)
      n++
      vertexData.put(points[2])
      vertexData.put(points[3])
      vertexData.put(1f)
      vertexData.put(0f)
      vertexData.put(alpha)
      n++
      vertexData.put(points[4])
      vertexData.put(points[5])
      vertexData.put(0f)
      vertexData.put(1f)
      vertexData.put(alpha)
      n++
      vertexData.put(points[6])
      vertexData.put(points[7])
      vertexData.put(1f)
      vertexData.put(1f)
      vertexData.put(alpha)
      n++
      if (i != count - 1) {
        vertexData.put(points[6])
        vertexData.put(points[7])
        vertexData.put(1f)
        vertexData.put(1f)
        vertexData.put(alpha)
        n++
      }
    }
    vertexData.position(0)
    val coordinatesData = vertexData.slice()
    GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, vertexDataSize, coordinatesData)
    GLES20.glEnableVertexAttribArray(0)
    vertexData.position(2)
    val textureData = vertexData.slice()
    GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, true, vertexDataSize, textureData)
    GLES20.glEnableVertexAttribArray(1)
    vertexData.position(4)
    val alphaData = vertexData.slice()
    GLES20.glVertexAttribPointer(2, 1, GLES20.GL_FLOAT, true, vertexDataSize, alphaData)
    GLES20.glEnableVertexAttribArray(2)
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, n)
    return dataBounds
  }
}