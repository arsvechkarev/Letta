package com.arsvechkarev.letta.openglcanvas.drawing

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.arsvechkarev.letta.openglcanvas.Logger

class Texture(private val bitmap: Bitmap) {
  
  val texture: Int
    get() {
      return if (bitmap.isRecycled) 0 else field
    }
  
  init {
    val textures = IntArray(1)
    GLES20.glGenTextures(1, textures, 0)
    texture = textures[0]
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
    Logger.printGLErrorIfAny()
  }
  
  fun cleanResources(recycleBitmap: Boolean) {
    val textures = intArrayOf(texture)
    GLES20.glDeleteTextures(1, textures, 0)
    if (recycleBitmap) {
      bitmap.recycle()
    }
  }
  
  companion object {
    
    @JvmStatic
    fun generate(size: Size): Int {
      val texture: Int
      val textures = IntArray(1)
      GLES20.glGenTextures(1, textures, 0)
      texture = textures[0]
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
      val width = size.width.toInt()
      val height = size.height.toInt()
      val format = GLES20.GL_RGBA
      val type = GLES20.GL_UNSIGNED_BYTE
      GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, format, width, height,
        0, format, type, null)
      return texture
    }
  }
}