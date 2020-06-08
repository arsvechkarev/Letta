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
  out[0] = a[0] * b[0] + a[4] * b[1] + a[8] * b[2] + a[12] * b[3]
  out[1] = a[1] * b[0] + a[5] * b[1] + a[9] * b[2] + a[13] * b[3]
  out[2] = a[2] * b[0] + a[6] * b[1] + a[10] * b[2] + a[14] * b[3]
  out[3] = a[3] * b[0] + a[7] * b[1] + a[11] * b[2] + a[15] * b[3]
  out[4] = a[0] * b[4] + a[4] * b[5] + a[8] * b[6] + a[12] * b[7]
  out[5] = a[1] * b[4] + a[5] * b[5] + a[9] * b[6] + a[13] * b[7]
  out[6] = a[2] * b[4] + a[6] * b[5] + a[10] * b[6] + a[14] * b[7]
  out[7] = a[3] * b[4] + a[7] * b[5] + a[11] * b[6] + a[15] * b[7]
  out[8] = a[0] * b[8] + a[4] * b[9] + a[8] * b[10] + a[12] * b[11]
  out[9] = a[1] * b[8] + a[5] * b[9] + a[9] * b[10] + a[13] * b[11]
  out[10] = a[2] * b[8] + a[6] * b[9] + a[10] * b[10] + a[14] * b[11]
  out[11] = a[3] * b[8] + a[7] * b[9] + a[11] * b[10] + a[15] * b[11]
  out[12] = a[0] * b[12] + a[4] * b[13] + a[8] * b[14] + a[12] * b[15]
  out[13] = a[1] * b[12] + a[5] * b[13] + a[9] * b[14] + a[13] * b[15]
  out[14] = a[2] * b[12] + a[6] * b[13] + a[10] * b[14] + a[14] * b[15]
  out[15] = a[3] * b[12] + a[7] * b[13] + a[11] * b[14] + a[15] * b[15]
  return out
}