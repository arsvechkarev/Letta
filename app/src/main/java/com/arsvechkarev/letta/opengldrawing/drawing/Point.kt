package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.PointF
import kotlin.math.pow
import kotlin.math.sqrt

data class Point(
  val x: Double = 0.0,
  val y: Double = 0.0
) {
  
  var edge = false
  
  fun multiplySum(point: Point, scalar: Double): Point {
    return Point((x + point.x) * scalar, (y + point.y) * scalar)
  }
  
  fun add(point: Point): Point {
    return Point(x + point.x, y + point.y)
  }
  
  fun subtract(point: Point): Point {
    return Point(x - point.x, y - point.y)
  }
  
  fun multiplyByScalar(scalar: Double): Point {
    return Point(x * scalar, y * scalar)
  }
  
  fun distanceTo(point: Point): Float {
    return sqrt((x - point.x).pow(2) + (y - point.y).pow(2)).toFloat()
  }
  
  fun toPointF(): PointF {
    return PointF(x.toFloat(), y.toFloat())
  }
}