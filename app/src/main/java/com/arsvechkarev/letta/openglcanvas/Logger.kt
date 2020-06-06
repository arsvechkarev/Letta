package com.arsvechkarev.letta.openglcanvas

import android.opengl.GLES20
import android.opengl.GLUtils
import timber.log.Timber

object Logger {
  
  fun error(s: String) {
    throw RuntimeException(s)
  }
  
  fun printGLErrorIfAny() {
    val error = GLES20.glGetError()
    if (error != 0) {
      Timber.d(GLUtils.getEGLErrorString(error))
    }
  }
}