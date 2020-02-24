package com.arsvechkarev.letta.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.arsvechkarev.letta.utils.dp

private var strokePaint: Paint? = null

val View.STROKE_PAINT: Paint
  get() {
    if (strokePaint == null) {
      strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2.dp
        setShadowLayer(4.dp, 0f, 0f, Color.BLACK);
        setLayerType(View.LAYER_TYPE_SOFTWARE, this);
      }
    }
    return strokePaint!!
  }

fun View.drawBounds(canvas: Canvas, color: Int = Color.RED) {
  canvas.drawRect(Rect(0, 0, width, height), Paint().apply {
    style = Paint.Style.STROKE
    this.color = color
    strokeWidth = 3f
  })
}