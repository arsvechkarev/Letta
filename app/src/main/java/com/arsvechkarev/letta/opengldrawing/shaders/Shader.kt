package com.arsvechkarev.letta.opengldrawing.shaders

import android.graphics.Color
import android.opengl.GLES20
import com.arsvechkarev.letta.opengldrawing.Logger
import java.util.HashMap

class Shader(
  vertexShaderCode: String,
  fragmentShaderCode: String,
  attributes: Array<String>,
  uniforms: Array<String>
) {
  
  val program = GLES20.glCreateProgram()
  
  private val vertexShader = 0
  private val uniformsMap: MutableMap<String, Int> = HashMap()
  
  init {
    val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
    val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
    GLES20.glAttachShader(program, vertexShader.shaderHandle)
    GLES20.glAttachShader(program, fragmentShader.shaderHandle)
    for (i in attributes.indices) {
      GLES20.glBindAttribLocation(program, i, attributes[i])
    }
    linkProgram(program)
    for (uniform in uniforms) {
      uniformsMap[uniform] = GLES20.glGetUniformLocation(program, uniform)
    }
    if (vertexShader.shaderHandle != 0) {
      GLES20.glDeleteShader(vertexShader.shaderHandle)
    }
    if (fragmentShader.shaderHandle != 0) {
      GLES20.glDeleteShader(fragmentShader.shaderHandle)
    }
  }
  
  fun cleanResources() {
    GLES20.glDeleteProgram(vertexShader)
  }
  
  fun getUniform(key: String): Int {
    return uniformsMap[key]!!
  }
  
  private fun compileShader(type: Int, shaderCode: String): CompiledShader {
    val shader = GLES20.glCreateShader(type)
    GLES20.glShaderSource(shader, shaderCode)
    GLES20.glCompileShader(shader)
    val compileStatus = IntArray(1)
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
    if (compileStatus[0] == GLES20.GL_FALSE) {
      Logger.error(GLES20.glGetProgramInfoLog(program))
    }
    return CompiledShader(shader)
  }
  
  private fun linkProgram(program: Int): Int {
    GLES20.glLinkProgram(program)
    val linkStatus = IntArray(1)
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
    if (linkStatus[0] == GLES20.GL_FALSE) {
      throw RuntimeException(GLES20.glGetProgramInfoLog(program))
    }
    return linkStatus[0]
  }
  
  companion object {
    
    @JvmStatic
    fun setColorUniform(location: Int, color: Int) {
      val r = Color.red(color) / 255.0f
      val g = Color.green(color) / 255.0f
      val b = Color.blue(color) / 255.0f
      val a = Color.alpha(color) / 255.0f
      GLES20.glUniform4f(location, r, g, b, a)
    }
  }
}
