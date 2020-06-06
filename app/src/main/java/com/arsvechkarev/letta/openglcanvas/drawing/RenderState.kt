package com.arsvechkarev.letta.openglcanvas.drawing

import android.graphics.PointF
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RenderState {
  
  var baseWeight = 0f
  var spacing = 0f
  var alpha = 0f
  var angle = 0f
  var scale = 0f
  var remainder = 0.0
  var count = 0
    private set
  
  private var allocatedCount = 0
  private var buffer: ByteBuffer? = null
  
  fun prepare() {
    count = 0
    if (buffer != null) {
      return
    }
    allocatedCount = DEFAULT_STATE_SIZE
    buffer = ByteBuffer.allocateDirect(allocatedCount * 5 * 4)
        .order(ByteOrder.nativeOrder())
    buffer!!.position(0)
  }
  
  fun read(): Float {
    return buffer!!.float
  }
  
  fun setPosition(position: Int) {
    if (buffer == null || position < 0 || position >= allocatedCount) {
      return
    }
    buffer!!.position(position * 5 * 4)
  }
  
  fun appendValuesCount(count: Int) {
    val newTotalCount = this.count + count
    if (newTotalCount > allocatedCount || buffer == null) {
      resizeBuffer()
    }
    this.count = newTotalCount
  }
  
  fun addPoint(point: PointF, size: Float, angle: Float, alpha: Float, index: Int): Boolean {
    val buffer = buffer!!
    if (index != -1 && index >= allocatedCount || buffer.position() == buffer.limit()) {
      resizeBuffer()
      return false
    }
    if (index != -1) {
      buffer.position(index * 5 * 4)
    }
    buffer.putFloat(point.x)
    buffer.putFloat(point.y)
    buffer.putFloat(size)
    buffer.putFloat(angle)
    buffer.putFloat(alpha)
    return true
  }
  
  fun reset() {
    count = 0
    remainder = 0.0
    buffer?.position(0)
  }
  
  private fun resizeBuffer() {
    if (buffer != null) {
      buffer = null
    }
    allocatedCount = (allocatedCount * 2).coerceAtLeast(DEFAULT_STATE_SIZE)
    buffer = ByteBuffer.allocateDirect(allocatedCount * 5 * 4)
        .order(ByteOrder.nativeOrder())
    buffer!!.position(0)
  }
  
  companion object {
    
    private const val DEFAULT_STATE_SIZE = 256
  }
}