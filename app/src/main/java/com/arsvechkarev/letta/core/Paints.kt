package com.arsvechkarev.letta.core

import android.graphics.Paint
import com.arsvechkarev.letta.utils.dp

private var strokePaint: Paint? = null
val STROKE_PAINT: Paint
  get() {
    if (strokePaint == null) {
      strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = COLOR_BORDER_LIGHT
        style = Paint.Style.STROKE
        strokeWidth = (1.2f).dp
      }
    }
    return strokePaint!!
  }