package com.arsvechkarev.letta.views.common

import android.graphics.PointF
import kotlin.math.abs
import kotlin.math.sqrt

class Circle {
  var x: Float = 0f
  var y: Float = 0f
  var radius: Float = 0f
  var color: Int = 0
  
  fun set(x: Float, y: Float, radius: Float, color: Int = 0) {
    this.x = x
    this.y = y
    this.radius = radius
    this.color = color
  }
  
  operator fun contains(pointF: PointF): Boolean {
    val absX = abs(pointF.x - x)
    val absY = abs(pointF.y - y)
    val distToCenter = sqrt(absX * absX + absY * absY)
    return distToCenter <= radius
  }
}