package com.arsvechkarev.letta.openglcanvas.drawing

interface Renderer {
  fun onBeganDrawing() {}
  fun onFinishedDrawing(moved: Boolean) {}
  fun shouldDraw() = false
}