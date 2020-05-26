package com.arsvechkarev.letta.views

import android.graphics.Bitmap
import android.graphics.Rect

class RoundedCornersColorDrawable(
  image: Bitmap,
  borderColor: Int,
  borderWidth: Float,
  cornersRadius: Float
) : RoundedCornersDrawable(image, borderColor, borderWidth, cornersRadius) {
  
  fun setColor(color: Int) {
    imagePaint.color = color
  }
}