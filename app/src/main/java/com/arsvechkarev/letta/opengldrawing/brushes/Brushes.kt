package com.arsvechkarev.letta.opengldrawing.brushes

import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.extensions.toRadians

object CircleBrush : Brush {
  override val spacing = 0.03f
  override val alpha = 0.85f
  override val stamp = R.drawable.brush_circle
}

object EllipticalBrush : Brush {
  override val spacing = 0.04f
  override val alpha = 0.5f
  override val stamp = R.drawable.brush_elliptical
  override val angleRadians = 125.toRadians()
}