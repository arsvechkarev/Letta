package com.arsvechkarev.letta.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

fun Drawable.toBitmap(): Bitmap {
  val bitmap = Bitmap.createBitmap(
    intrinsicWidth,
    intrinsicHeight,
    Bitmap.Config.ARGB_8888
  )
  val canvas = Canvas(bitmap)
  setBounds(0, 0, canvas.width, canvas.height)
  draw(canvas)
  return bitmap
}

inline fun Canvas.execute(block: Canvas.() -> Unit) {
  try {
    save()
    apply(block)
  } finally {
    restore()
  }
}

/* ------------------------------------------- */
/* ----------------  Colors  ----------------- */
/* ------------------------------------------- */

fun Int.isWhiterThan(limitChannel: Int): Boolean {
  val r = this and 0x00FF0000 shr 16
  val g = this and 0x0000FF00 shr 8
  val b = this and 0x000000FF
  return r > limitChannel && g > limitChannel && b > limitChannel
      && r == g && r == b && g == b
}
