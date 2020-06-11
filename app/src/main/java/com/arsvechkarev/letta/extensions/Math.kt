package com.arsvechkarev.letta.extensions

import android.graphics.Matrix

val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

fun Int.toRadians(): Float = Math.toRadians(this.toDouble()).toFloat()

fun FloatArray.orthoM(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
  android.opengl.Matrix.orthoM(this, 0, left, right, bottom, top, near, far)
}

fun Matrix.to4x4Matrix(): FloatArray {
  val result = FloatArray(16)
  val inputMatrix = FloatArray(9)
  getValues(inputMatrix)
  result[0] = inputMatrix[Matrix.MSCALE_X]
  result[1] = inputMatrix[Matrix.MSKEW_X]
  result[2] = 0.0f
  result[3] = 0.0f
  result[4] = inputMatrix[Matrix.MSKEW_Y]
  result[5] = inputMatrix[Matrix.MSCALE_Y]
  result[6] = 0.0f
  result[7] = 0.0f
  result[8] = 0.0f
  result[9] = 0.0f
  result[10] = 1.0f
  result[11] = 0.0f
  result[12] = inputMatrix[Matrix.MTRANS_X]
  result[13] = inputMatrix[Matrix.MTRANS_Y]
  result[14] = 0.0f
  result[15] = 1.0f
  return result
}

fun multiplyMatrices(a: FloatArray, b: FloatArray): FloatArray {
  val out = FloatArray(16)
  android.opengl.Matrix.multiplyMM(out, 0, a, 0, b, 0)
  return out
}