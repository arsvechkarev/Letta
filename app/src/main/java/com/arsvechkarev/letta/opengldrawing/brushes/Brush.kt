package com.arsvechkarev.letta.opengldrawing.brushes

import android.content.res.Resources
import android.graphics.Bitmap

interface Brush {
  val spacing: Float
  val alpha: Float
  val angle: Float
  val scale: Float
  val isLightSaber: Boolean
  fun getStamp(resources: Resources): Bitmap
}