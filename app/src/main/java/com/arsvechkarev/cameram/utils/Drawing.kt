package com.arsvechkarev.cameram.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable

fun Drawable.toBitmap(): Bitmap {
  val bitmap = Bitmap.createBitmap(
    intrinsicWidth,
    intrinsicHeight, Bitmap.Config.ARGB_8888
  )
  val canvas = Canvas(bitmap)
  setBounds(0, 0, canvas.width, canvas.height)
  draw(canvas)
  return bitmap
}