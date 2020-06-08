package com.arsvechkarev.letta.opengldrawing.brushes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

interface Brush {
  val spacing: Float
  val alpha: Float
  val stamp: Int
  val angleRadians: Float
    get() = 0f
  val scale: Float
    get() = 1f
  val isLightSaber: Boolean
    get() = false
  
  fun getStamp(context: Context): Bitmap {
    val options = BitmapFactory.Options()
    options.inScaled = false
    return BitmapFactory.decodeResource(context.resources, stamp, options)
  }
}