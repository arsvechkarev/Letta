package com.arsvechkarev.letta.graphics

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.arsvechkarev.letta.utils.dp

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

private var shadowStrokePaint: Paint? = null
val SHADOW_STROKE_PAINT: Paint
  get() {
    if (shadowStrokePaint == null) {
      shadowStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = (0.5f).dp
        maskFilter = BlurMaskFilter(2.dp, Blur.NORMAL)
      }
    }
    return shadowStrokePaint!!
  }

fun shadowPaint(blurType: Blur): Paint {
  return SHADOW_STROKE_PAINT.apply {
    maskFilter = BlurMaskFilter(4f, blurType)
  }
}

fun View.drawBounds(canvas: Canvas, color: Int = Color.RED) {
  canvas.drawRect(Rect(0, 0, width, height), Paint().apply {
    style = Paint.Style.STROKE
    this.color = color
    strokeWidth = 3f
  })
}