package com.arsvechkarev.letta.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import com.arsvechkarev.letta.BuildConfig


val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

/**
 * Deconstructing motion event:
 *    val (x, y) = event
 */
operator fun MotionEvent.component1() = this.x

operator fun MotionEvent.component2() = this.x

fun MotionEvent.toPointF() = PointF(x, y)

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
  save()
  apply(block)
  restore()
}

fun RectF.toRect(): Rect {
  return Rect(left.i, top.i, right.i, bottom.i)
}

// Only in debug
fun View.drawBounds(canvas: Canvas, color: Int = Color.RED) {
  if (!BuildConfig.DEBUG) {
    throw IllegalStateException("Nope")
  }
  canvas.drawRect(Rect(0, 0, width, height), Paint().apply {
    style = Paint.Style.STROKE
    this.color = color
    strokeWidth = 5f
  })
}

val String.c get() = Color.parseColor(this)

fun Int.isWhiterThan(limitChannel: Int): Boolean {
  val r = this and 0x00FF0000 shr 16
  val g = this and 0x0000FF00 shr 8
  val b = this and 0x000000FF
  return r > limitChannel && g > limitChannel && b > limitChannel
      && r == g && r == b && g == b
}
