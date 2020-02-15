package com.arsvechkarev.letta.utils

import android.graphics.PointF
import android.view.MotionEvent

val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

/**
 * Deconstructing motion event:
 *    val (x, y) = event
 */
operator fun MotionEvent.component1() = this.x

operator fun MotionEvent.component2() = this.x

fun MotionEvent.toPointF() = PointF(x, y)