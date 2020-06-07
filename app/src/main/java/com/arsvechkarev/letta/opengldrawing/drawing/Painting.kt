package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.Bitmap
import android.graphics.RectF
import android.opengl.GLES20
import com.arsvechkarev.letta.extensions.multiplyMatrices
import com.arsvechkarev.letta.extensions.orthoM
import com.arsvechkarev.letta.extensions.to4x4Matrix
import com.arsvechkarev.letta.opengldrawing.Action
import com.arsvechkarev.letta.opengldrawing.Logger
import com.arsvechkarev.letta.opengldrawing.brushes.Brush
import com.arsvechkarev.letta.opengldrawing.shaders.Shader
import com.arsvechkarev.letta.opengldrawing.shaders.ShaderSet
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Painting(
  val size: Size,
  private val painter: Painter,
  private val openGLDrawingView: OpenGLDrawingView,
  private var brush: Brush
) {
  
  private lateinit var bitmapTexture: Texture
  private lateinit var renderProjection: FloatArray
  private lateinit var shaders: Map<String, Shader>
  
  private var activePath: Path? = null
  private var activeStrokeBounds: RectF? = null
  private var brushTexture: Texture? = null
  private var reusableFramebuffer = 0
  private var paintTexture = 0
  private var suppressChangesCounter = 0
  private var backupSlice: Slice? = null
  
  private var paused = false
  private val projection = FloatArray(16)
  private val buffers = IntArray(1)
  private val renderState = RenderState()
  private val vertexBuffer: ByteBuffer
  private val textureBuffer: ByteBuffer
  private val dataBuffer = ByteBuffer.allocateDirect(size.width.toInt() * size.height.toInt() * 4)
  
  init {
    projection.orthoM(0f, size.width, 0f, size.height, -1.0f, 1.0f)
    vertexBuffer = ByteBuffer.allocateDirect(8 * 4)
    vertexBuffer.order(ByteOrder.nativeOrder())
    vertexBuffer.putFloat(0.0f)
    vertexBuffer.putFloat(0.0f)
    vertexBuffer.putFloat(size.width)
    vertexBuffer.putFloat(0.0f)
    vertexBuffer.putFloat(0.0f)
    vertexBuffer.putFloat(size.height)
    vertexBuffer.putFloat(size.width)
    vertexBuffer.putFloat(size.height)
    vertexBuffer.rewind()
    textureBuffer = ByteBuffer.allocateDirect(8 * 4)
    textureBuffer.order(ByteOrder.nativeOrder())
    textureBuffer.putFloat(0.0f)
    textureBuffer.putFloat(0.0f)
    textureBuffer.putFloat(1.0f)
    textureBuffer.putFloat(0.0f)
    textureBuffer.putFloat(0.0f)
    textureBuffer.putFloat(1.0f)
    textureBuffer.putFloat(1.0f)
    textureBuffer.putFloat(1.0f)
    textureBuffer.rewind()
  }
  
  fun setupShaders() {
    shaders = ShaderSet.setup()
  }
  
  fun setRenderProjection(projection: FloatArray) {
    renderProjection = projection
  }
  
  fun setBitmap(bitmap: Bitmap?) {
    bitmapTexture = Texture(bitmap!!)
  }
  
  fun render() {
    if (activePath != null) {
      render(getPaintTexture(), activePath!!.color)
    } else {
      renderBlit()
    }
  }
  
  fun paintStroke(path: Path, clearBuffer: Boolean, action: Action) {
    openGLDrawingView.performInEGLContext {
      activePath = path
  
      var bounds: RectF? = null
  
      GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, getReusableFramebuffer())
      GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
        GLES20.GL_TEXTURE_2D, getPaintTexture(), 0)
  
      Logger.printGLErrorIfAny()
      
      val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
      
      if (status == GLES20.GL_FRAMEBUFFER_COMPLETE) {
        GLES20.glViewport(0, 0, size.width.toInt(), size.height.toInt())
        
        if (clearBuffer) {
          GLES20.glClearColor(0f, 0f, 0f, 0f)
          GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        }
        val key = if (brush.isLightSaber) "brushLight" else "brush"
        val shader = shaders.getValue(key)
        GLES20.glUseProgram(shader.program)
        if (brushTexture == null) {
          brushTexture = Texture(brush.getStamp(openGLDrawingView.context.resources))
        }
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, brushTexture!!.texture)
        GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false,
          FloatBuffer.wrap(projection))
        GLES20.glUniform1i(shader.getUniform("texture"), 0)
        bounds = RenderUtils.renderPath(path, renderState)
      }
      
      GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
      
      painter.onContentChanged(bounds)
      
      if (activeStrokeBounds != null) {
        activeStrokeBounds!!.union(bounds!!)
      } else {
        activeStrokeBounds = bounds
      }
  
      action()
    }
  }
  
  fun commitStroke(color: Int) {
    openGLDrawingView.performInEGLContext {
      registerUndo(activeStrokeBounds)
  
      beginSuppressingChanges()
  
      update {
        val key = if (brush.isLightSaber) "compositeWithMaskLight" else "compositeWithMask"
        val shader = shaders.getValue(key)
    
        GLES20.glUseProgram(shader.program)
    
        GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false,
          FloatBuffer.wrap(projection))
        GLES20.glUniform1i(shader.getUniform("mask"), 0)
        Shader.setColorUniform(shader.getUniform("color"), color)
    
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getPaintTexture())
    
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA,
          GLES20.GL_SRC_ALPHA, GLES20.GL_ONE)
    
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer)
        GLES20.glEnableVertexAttribArray(0)
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 8, textureBuffer)
        GLES20.glEnableVertexAttribArray(1)
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
      }
      
      endSuppressingChanges()
      
      renderState.reset()
      
      activeStrokeBounds = null
      activePath = null
    }
  }
  
  fun getPaintingData(rect: RectF, undo: Boolean): PaintingData {
    val minX = rect.left.toInt()
    val minY = rect.top.toInt()
    val width = rect.width().toInt()
    val height = rect.height().toInt()
  
    GLES20.glGenFramebuffers(1, buffers, 0)
  
    val framebuffer = buffers[0]
    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer)
    GLES20.glGenTextures(1, buffers, 0)
  
    val texture = buffers[0]
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
    GLES20
        .glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
    GLES20
        .glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
    GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
      GL10.GL_CLAMP_TO_EDGE)
    GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
      GL10.GL_CLAMP_TO_EDGE)
    GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
      GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
  
    GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
      GLES20.GL_TEXTURE_2D, texture, 0)
  
    GLES20.glViewport(0, 0, size.width.toInt(), size.height.toInt())
    val shader = shaders[if (undo) "nonPremultipliedBlit" else "blit"]!!
  
    GLES20.glUseProgram(shader.program)
  
    val translate = android.graphics.Matrix()
    translate.preTranslate(-minX.toFloat(), -minY.toFloat())
    val effective = translate.to4x4Matrix()
    val finalProjection = multiplyMatrices(projection, effective)
  
    GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false,
      FloatBuffer.wrap(finalProjection))
  
    if (undo) {
      GLES20.glUniform1i(shader.getUniform("texture"), 0)
    
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTexture())
    } else {
      GLES20.glUniform1i(shader.getUniform("texture"), 0)
    
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bitmapTexture.texture)
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTexture())
    }
    GLES20.glClearColor(0f, 0f, 0f, 0f)
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer)
    GLES20.glEnableVertexAttribArray(0)
    GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 8, textureBuffer)
    GLES20.glEnableVertexAttribArray(1)
  
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
  
    dataBuffer.limit(width * height * 4)
    GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
      dataBuffer)
  
    val data: PaintingData
    data = if (undo) {
      PaintingData(null, dataBuffer)
    } else {
      val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
      bitmap.copyPixelsFromBuffer(dataBuffer)
      PaintingData(bitmap, null)
    }
    buffers[0] = framebuffer
    GLES20.glDeleteFramebuffers(1, buffers, 0)
    buffers[0] = texture
    GLES20.glDeleteTextures(1, buffers, 0)
    return data
  }
  
  fun setBrush(value: Brush) {
    brush = value
    brushTexture?.cleanResources(true)
    brushTexture = null
  }
  
  fun isPaused(): Boolean {
    return paused
  }
  
  fun onPause(completionAction: Action) {
    openGLDrawingView.performInEGLContext {
      paused = true
      val data = getPaintingData(bounds, true)
      backupSlice = Slice(data.byteBuffer!!, bounds)
      cleanResources(false)
      completionAction()
    }
  }
  
  fun onResume() {
    restoreSlice(backupSlice!!)
    backupSlice = null
    paused = false
  }
  
  fun cleanResources(recycle: Boolean) {
    if (reusableFramebuffer != 0) {
      buffers[0] = reusableFramebuffer
      GLES20.glDeleteFramebuffers(1, buffers, 0)
      reusableFramebuffer = 0
    }
    
    bitmapTexture.cleanResources(recycle)
    
    if (paintTexture != 0) {
      buffers[0] = paintTexture
      GLES20.glDeleteTextures(1, buffers, 0)
      paintTexture = 0
    }
    if (brushTexture != null) {
      brushTexture!!.cleanResources(true)
      brushTexture = null
    }
    for (shader in shaders.values) {
      shader.cleanResources()
    }
  }
  
  private val bounds: RectF
    get() {
      return RectF(0.0f, 0.0f, size.width, size.height)
    }
  
  private val isSuppressingChanges: Boolean
    get() {
      return suppressChangesCounter > 0
    }
  
  private fun beginSuppressingChanges() {
    suppressChangesCounter++
  }
  
  private fun endSuppressingChanges() {
    suppressChangesCounter--
  }
  
  private fun render(mask: Int, color: Int) {
    val s = if (brush.isLightSaber) "blitWithMaskLight" else "blitWithMask"
    val shader = shaders.getValue(s)
  
    GLES20.glUseProgram(shader.program)
  
    GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false,
      FloatBuffer.wrap(renderProjection))
    GLES20.glUniform1i(shader.getUniform("texture"), 0)
    GLES20.glUniform1i(shader.getUniform("mask"), 1)
    Shader.setColorUniform(shader.getUniform("color"), color)
  
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTexture())
  
    GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mask)
  
    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
  
    GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer)
    GLES20.glEnableVertexAttribArray(0)
    GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 8, textureBuffer)
    GLES20.glEnableVertexAttribArray(1)
    
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    
    Logger.printGLErrorIfAny()
  }
  
  private fun renderBlit() {
    val shader = shaders.getValue("blit")
    GLES20.glUseProgram(shader.program)
  
    GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false,
      FloatBuffer.wrap(renderProjection))
    GLES20.glUniform1i(shader.getUniform("texture"), 0)
  
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTexture())
    
    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    
    GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer)
    GLES20.glEnableVertexAttribArray(0)
    GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 8, textureBuffer)
    GLES20.glEnableVertexAttribArray(1)
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    Logger.printGLErrorIfAny()
  }
  
  private fun update(action: Action) {
    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, getReusableFramebuffer())
    GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
      GLES20.GL_TEXTURE_2D, getTexture(), 0)
    
    val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
    if (status == GLES20.GL_FRAMEBUFFER_COMPLETE) {
      GLES20.glViewport(0, 0, size.width.toInt(), size.height.toInt())
      action()
    }
    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    
    if (!isSuppressingChanges) {
      painter.onContentChanged(null)
    }
  }
  
  private fun registerUndo(rect: RectF?) {
    if (rect == null) {
      return
    }
    val intersect = rect.setIntersect(rect, bounds)
    if (!intersect) {
      return
    }
    val paintingData: PaintingData = getPaintingData(rect, true)
    val data = paintingData.byteBuffer
    val slice = Slice(data!!, rect)
    painter.undoStore.registerUndo { restoreSlice(slice) }
  }
  
  private fun restoreSlice(slice: Slice) {
    openGLDrawingView.performInEGLContext {
      val buffer = slice.data
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTexture())
      GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, slice.x, slice.y,
        slice.width, slice.height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
        buffer)
      if (!isSuppressingChanges) {
        painter.onContentChanged(slice.getBounds())
      }
      slice.cleanResources()
    }
  }
  
  private fun getReusableFramebuffer(): Int {
    if (reusableFramebuffer == 0) {
      val buffers = IntArray(1)
      GLES20.glGenFramebuffers(1, buffers, 0)
      reusableFramebuffer = buffers[0]
      Logger.printGLErrorIfAny()
    }
    return reusableFramebuffer
  }
  
  private fun getTexture(): Int {
    return bitmapTexture.texture
  }
  
  private fun getPaintTexture(): Int {
    if (paintTexture == 0) {
      paintTexture = Texture.generate(size)
    }
    return paintTexture
  }
}