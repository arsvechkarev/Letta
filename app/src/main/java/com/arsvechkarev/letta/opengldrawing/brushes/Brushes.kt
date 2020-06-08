package com.arsvechkarev.letta.opengldrawing.brushes

import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.extensions.toRadians

object CircleBrush : Brush {
  override val spacing = 0.08f
  override val alpha = 0.8f
  override val stamp = R.drawable.brush_circle
}

object EllipticalBrush : Brush {
  override val spacing = 0.08f
  override val alpha = 0.8f
  override val angleRadians = (-30).toRadians()
  override val stamp = R.drawable.brush_elliptical
}

object DotsBrush : Brush {
  override val spacing = 0.3f
  override val alpha = 0.2f
  override val isAngleRandom = true
  override val stamp = R.drawable.brush_dots
}

object LinesBrush : Brush {
  override val spacing = 0.2f
  override val alpha = 0.3f
  override val stamp = R.drawable.brush_lines
}

val BRUSHES = listOf(CircleBrush, EllipticalBrush, DotsBrush, LinesBrush)