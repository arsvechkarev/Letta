package com.arsvechkarev.letta.views

import android.graphics.Bitmap
import android.graphics.Rect

class RoundedCornersColorDrawable(
  image: Bitmap,
  borderColor: Int,
  borderWidth: Float,
  cornersRadius: Float
) : RoundedCornersDrawable(image, borderColor, borderWidth, cornersRadius) {
  
  override fun onBoundsChange(bounds: Rect) {
    super.onBoundsChange(bounds)
    imagePaint.shader = null
  }
  
  fun setColor(color: Int) {
    imagePaint.color = color
  }
}