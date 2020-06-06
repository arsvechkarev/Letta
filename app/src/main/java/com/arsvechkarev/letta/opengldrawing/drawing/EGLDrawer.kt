package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLUtils
import android.os.Looper
import com.arsvechkarev.letta.opengldrawing.Action
import com.arsvechkarev.letta.opengldrawing.DispatchQueue
import com.arsvechkarev.letta.opengldrawing.Logger
import java.util.concurrent.CountDownLatch
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay
import javax.microedition.khronos.egl.EGLSurface

class EGLDrawer(
  private val surfaceTexture: SurfaceTexture,
  private var bitmap: Bitmap,
  private val painting: Painting,
  private val queue: DispatchQueue
) : DispatchQueue("EGLDrawerThread") {
  
  private lateinit var egl10: EGL10
  private var eglDisplay: EGLDisplay? = null
  private var eglContext: EGLContext? = null
  private var eglSurface: EGLSurface? = null
  
  var isInitialized = false
    private set
  var isReady = false
    private set
  var bufferWidth = 0
    private set
  var bufferHeight = 0
    private set
  
  private var scheduledRunnable: Action? = null
  
  private val drawRunnable = label@{
    if (!isInitialized) {
      return@label
    }
    setCurrentContext()
    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    GLES20.glViewport(0, 0, bufferWidth, bufferHeight)
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    painting.render()
    GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    egl10.eglSwapBuffers(eglDisplay, eglSurface)
    if (!isReady) {
      queue.postRunnable(200) { isReady = true }
    }
  }
  
  fun setCurrentContext(): Boolean {
    if (!isInitialized) {
      return false
    }
    val egl10 = egl10
    val makeCurrent = egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    val eglGetCurrentSurface = egl10.eglGetCurrentSurface(EGL10.EGL_DRAW)
    if (eglContext != egl10.eglGetCurrentContext() || eglSurface != eglGetCurrentSurface) {
      return makeCurrent
    }
    return true
  }
  
  fun setBufferSize(width: Int, height: Int) {
    bufferWidth = width
    bufferHeight = height
  }
  
  fun requestRender() {
    postRunnable(drawRunnable)
  }
  
  fun scheduleRedraw() {
    if (scheduledRunnable != null) {
      cancelRunnable(scheduledRunnable!!)
      scheduledRunnable = null
    }
    scheduledRunnable = {
      drawRunnable()
      scheduledRunnable = null
    }
    postRunnable(scheduledRunnable!!)
  }
  
  fun shutdown() {
    postRunnable {
      finish()
      Looper.myLooper()!!.quit()
    }
  }
  
  fun getTexture(): Bitmap {
    if (!isInitialized) {
      error("Not initialized yet")
    }
    val latch = CountDownLatch(1)
    val bitmaps = arrayOfNulls<Bitmap>(1)
    postRunnable {
      val data = painting.getPaintingData(
        RectF(0f, 0f, painting.size.width, painting.size.height),
        false)
      bitmaps[0] = data.bitmap
      latch.countDown()
    }
    latch.await()
    return bitmaps[0]!!
  }
  
  override fun run() {
    if (bitmap.isRecycled) {
      return
    }
    isInitialized = initializeGL()
    super.run()
  }
  
  private fun initializeGL(): Boolean {
    egl10 = EGLContext.getEGL() as EGL10
    eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
    if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
      Logger.error("eglGetDisplay failed " + GLUtils.getEGLErrorString(egl10.eglGetError()))
    }
    val version = IntArray(2)
    if (!egl10.eglInitialize(eglDisplay, version)) {
      Logger.error("eglInitialize failed " + GLUtils.getEGLErrorString(egl10.eglGetError()))
    }
    val configsCount = IntArray(1)
    val configs = arrayOfNulls<EGLConfig>(1)
    val configSpec = intArrayOf(
      EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
      EGL10.EGL_RED_SIZE, 8,
      EGL10.EGL_GREEN_SIZE, 8,
      EGL10.EGL_BLUE_SIZE, 8,
      EGL10.EGL_ALPHA_SIZE, 8,
      EGL10.EGL_DEPTH_SIZE, 0,
      EGL10.EGL_STENCIL_SIZE, 0,
      EGL10.EGL_NONE
    )
    val chooseConfig = egl10.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)
    if (!chooseConfig) {
      Logger.error("eglChooseConfig failed " + GLUtils.getEGLErrorString(egl10.eglGetError()))
    }
    if (configsCount[0] <= 0) {
      Logger.error("eglConfig is not initialized")
    }
    val eglConfig = configs[0]
    val attrs = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
    eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrs)
    if (eglContext == null) {
      Logger.error("eglCreateContext failed " + GLUtils.getEGLErrorString(egl10.eglGetError()))
    }
    eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, null)
    if (eglSurface == null || eglSurface === EGL10.EGL_NO_SURFACE) {
      Logger.error("createWindowSurface failed " + GLUtils.getEGLErrorString(egl10.eglGetError()))
    }
    if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
      Logger.error("eglMakeCurrent failed " + GLUtils.getEGLErrorString(egl10.eglGetError()))
    }
    GLES20.glEnable(GLES20.GL_BLEND)
    GLES20.glDisable(GLES20.GL_DITHER)
    GLES20.glDisable(GLES20.GL_STENCIL_TEST)
    GLES20.glDisable(GLES20.GL_DEPTH_TEST)
    painting.setupShaders()
    checkBitmap()
    painting.setBitmap(bitmap)
    Logger.printGLErrorIfAny()
    return true
  }
  
  private fun createBitmap(bitmap: Bitmap, scale: Float): Bitmap {
    val matrix = Matrix()
    matrix.setScale(scale, scale)
    return Bitmap
        .createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
  }
  
  private fun checkBitmap() {
    val paintingSize = painting.size
    if (bitmap.width.toFloat() != paintingSize.width || bitmap.height.toFloat() != paintingSize.height) {
      val bitmapWidth = bitmap.width.toFloat()
      val scale = paintingSize.width / bitmapWidth
      bitmap = createBitmap(bitmap, scale)
    }
  }
  
  private fun finish() {
    if (eglSurface != null) {
      egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
        EGL10.EGL_NO_CONTEXT)
      egl10.eglDestroySurface(eglDisplay, eglSurface)
      eglSurface = null
    }
    if (eglContext != null) {
      egl10.eglDestroyContext(eglDisplay, eglContext)
      eglContext = null
    }
    if (eglDisplay != null) {
      egl10.eglTerminate(eglDisplay)
      eglDisplay = null
    }
  }
  
  companion object {
    private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
    private const val EGL_OPENGL_ES2_BIT = 4
  }
}