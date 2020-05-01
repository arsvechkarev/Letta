package com.arsvechkarev.letta.utils

import android.graphics.Paint

private var strokePaint: Paint? = null
val STROKE_PAINT: Paint
  get() {
    if (strokePaint == null) {
      strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = LIGHT_GRAY
        style = Paint.Style.STROKE
        strokeWidth = (1.2f).dp
      }
    }
    return strokePaint!!
  }