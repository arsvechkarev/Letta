package com.arsvechkarev.letta.openglcanvas.drawing

import com.arsvechkarev.letta.openglcanvas.brushes.Brush

class Path(points: Array<Point?>) {
  
  var remainder = 0.0
  var color = 0
    private set
  var baseWeight = 0f
    private set
  lateinit var brush: Brush
    private set
  
  val points = ArrayList<Point?>().apply { addAll(points) }
  
  val length: Int get() = points.size
  
  fun setup(color: Int, baseWeight: Float, brush: Brush) {
    this.color = color
    this.baseWeight = baseWeight
    this.brush = brush
  }
}