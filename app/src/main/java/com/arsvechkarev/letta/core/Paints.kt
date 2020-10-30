package com.arsvechkarev.letta.core

import android.graphics.Paint
import com.arsvechkarev.letta.extensions.dp

private var strokePaint: Paint? = null
val STROKE_PAINT: Paint
  get() {
    if (strokePaint == null) {
      strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Colors.BorderLight
        style = Paint.Style.STROKE
        strokeWidth = 0.8f.dp
      }
    }
    return strokePaint!!
  }