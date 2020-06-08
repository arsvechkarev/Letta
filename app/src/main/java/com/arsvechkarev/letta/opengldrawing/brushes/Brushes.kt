package com.arsvechkarev.letta.opengldrawing.brushes

import com.arsvechkarev.letta.R

object CircleBrush : Brush {
  override val spacing = 0.03f
  override val alpha = 0.85f
  override val stamp = R.drawable.brush_circle
}

object EllipticalBrush : Brush {
  override val spacing = 0.8f
  override val alpha = 0.9f
  override val stamp = R.drawable.brush_dots
  override val isAngleRandom = true
}