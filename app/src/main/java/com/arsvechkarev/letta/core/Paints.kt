package com.arsvechkarev.letta.core

import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import com.arsvechkarev.letta.extensions.dp

val TEMP_PAINT = Paint(ANTI_ALIAS_FLAG)

val STROKE_PAINT by lazy {
  Paint(ANTI_ALIAS_FLAG).apply {
    color = Colors.BorderLight
    style = Paint.Style.STROKE
    strokeWidth = 0.8f.dp
  }
}