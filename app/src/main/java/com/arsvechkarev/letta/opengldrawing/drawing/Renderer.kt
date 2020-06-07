package com.arsvechkarev.letta.opengldrawing.drawing

interface Renderer {
  fun onBeganDrawing() {}
  fun onFinishedDrawing(moved: Boolean) {}
  fun shouldDraw() = true
}