package com.arsvechkarev.letta.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import kotlin.math.ceil
import kotlin.math.floor

val TEMP_RECT_F = RectF()

val TEMP_RECT = Rect()

fun Drawable.toBitmap(width: Int = intrinsicWidth, height: Int = intrinsicHeight): Bitmap {
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  setBounds(0, 0, canvas.width, canvas.height)
  draw(canvas)
  return bitmap
}

inline fun Canvas.execute(block: Canvas.() -> Unit) {
  save()
  apply(block)
  restore()
}

fun RectF.toRect(): Rect {
  return Rect(left.i, top.i, right.i, bottom.i)
}

fun RectF.roundToInts() {
  left = floor(left)
  top = floor(top)
  right = ceil(right)
  bottom = ceil(bottom)
}