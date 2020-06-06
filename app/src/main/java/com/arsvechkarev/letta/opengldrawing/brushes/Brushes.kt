package com.arsvechkarev.letta.opengldrawing.brushes

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.arsvechkarev.letta.R

class PlainBrush : Brush {
  
  override val spacing = 0.3f
  override val alpha = 0.85f
  override val angle = 0f
  override val scale = 1f
  override val isLightSaber = false
  
  override fun getStamp(resources: Resources): Bitmap {
    val options = BitmapFactory.Options()
    options.inScaled = false
    return BitmapFactory.decodeResource(resources, R.drawable.paint_dots, options)
  }
}

class EllipticalBrush : Brush {
  
  override val spacing = 0.04f
  override val alpha = 0.2f
  override val angle = Math.toRadians(125.0).toFloat()
  override val scale = 1.5f
  override val isLightSaber = false
  
  override fun getStamp(resources: Resources): Bitmap {
    val options = BitmapFactory.Options()
    options.inScaled = false
    return BitmapFactory.decodeResource(resources, R.drawable.paint_elliptical_brush, options)
  }
}