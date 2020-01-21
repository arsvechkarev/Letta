package com.arsvechkarev.cameram.extensions

import android.graphics.PointF
import android.view.MotionEvent

/**
 * Shorter way to write toFloat()
 */
val Int.f get() = this.toFloat()

/**
 * Deconstructing motion event:
 *    val (x, y) = event
 */
operator fun MotionEvent.component1() = this.x

operator fun MotionEvent.component2() = this.x

fun MotionEvent.toPointF() = PointF(x, y)