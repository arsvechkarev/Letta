package com.arsvechkarev.letta

import com.arsvechkarev.letta.extensions.withAlpha
import org.junit.Test

class Colors {
  
  @Test
  fun `Adding alpha to color`() {
    val color = 0x00FF0000
    val alpha = 0x42
    
    val result = color.withAlpha(alpha)
    
    println(Integer.toHexString(result))
  }
}