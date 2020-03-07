package com.arsvechkarev.letta.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.arsvechkarev.letta.utils.extenstions.dp

private var strokePaint: Paint? = null
val STROKE_PAINT: Paint
  get() {
    if (strokePaint == null) {
      strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = LIGHT_GRAY
        style = Paint.Style.STROKE
        strokeWidth = (0.5f).dp
      }
    }
    return strokePaint!!
  }

private var strokePaintLight: Paint? = null
val STROKE_PAINT_LIGHT: Paint
  get() {
    if (strokePaintLight == null) {
      strokePaintLight = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = LIGHT_GRAY
        style = Paint.Style.STROKE
        strokeWidth = (0.2f).dp
      }
    }
    return strokePaintLight!!
  }

fun View.drawBounds(canvas: Canvas, color: Int = Color.RED) {
  canvas.drawRect(Rect(0, 0, width, height), Paint().apply {
    style = Paint.Style.STROKE
    this.color = color
    strokeWidth = 3f
  })
}