package com.arsvechkarev.letta.extensions

import org.junit.Test

class GraphicsKtTest {
  
  @Test
  fun name() {
    val color = 0xff123456.toInt()
    println(Integer.toHexString(color))
    val newColor = color.withAlpha(42)
    println(Integer.toHexString(newColor))
  }
}