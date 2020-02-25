package com.arsvechkarev.letta.graphics

const val LIGHT_GRAY = 0xFFBBBBBB.toInt()
const val VERY_LIGHT_GRAY = 0xFFDDDDDD.toInt()

fun Int.isWhiterThan(limitChannel: Int): Boolean {
  val r = this and 0xFF0000 shr 16
  val g = this and 0x00FF00 shr 8
  val b = this and 0x0000FF
  return r > limitChannel && g > limitChannel && b > limitChannel
      && r == g && r == b && g == b
}