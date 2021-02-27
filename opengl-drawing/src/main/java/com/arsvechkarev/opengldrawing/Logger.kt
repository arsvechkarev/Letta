package com.arsvechkarev.opengldrawing

import android.opengl.GLES20
import android.opengl.GLUtils

object Logger {
  
  fun error(s: String) {
    throw RuntimeException(s)
  }
  
  fun printGLErrorIfAny() {
    val error = GLES20.glGetError()
    if (error != 0) {
      println("Logger ${GLUtils.getEGLErrorString(error)}")
    }
  }
}