package com.arsvechkarev.letta.opengldrawing.drawing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.view.MotionEvent
import android.view.TextureView
import com.arsvechkarev.letta.extensions.multiplyMatrices
import com.arsvechkarev.letta.extensions.orthoM
import com.arsvechkarev.letta.extensions.to4x4Matrix
import com.arsvechkarev.letta.opengldrawing.Action
import com.arsvechkarev.letta.opengldrawing.DispatchQueue
import com.arsvechkarev.letta.opengldrawing.UndoStore
import com.arsvechkarev.letta.opengldrawing.brushes.Brush

@SuppressLint("ViewConstructor")
class OpenGLDrawingView(
  context: Context,
  paintingSize: Size,
  var currentBrush: Brush,
  private val undoStore: UndoStore,
  private var backgroundBitmap: Bitmap,
  private var queue: DispatchQueue,
  private val renderer: Renderer
) : TextureView(context) {
  
  private val transformedBitmap = false
  private val inputProcessor = InputProcessor(this)
  private var eglDrawer: EGLDrawer? = null
  private var shuttingDown = false
  
  val painting: Painting
  var currentWeight = 0f
    private set
  var currentColor = 0
    private set
  
  init {
    val painter = object : Painter {
      override fun onContentChanged(rect: RectF?) {
        eglDrawer?.scheduleRedraw()
      }
  
      override val undoStore = this@OpenGLDrawingView.undoStore
    }
    painting = Painting(paintingSize, painter, this, currentBrush)
    surfaceTextureListener = object : SurfaceTextureListener {
      
      override fun onSurfaceTextureAvailable(
        surface: SurfaceTexture,
        width: Int,
        height: Int
      ) {
        eglDrawer = EGLDrawer(surface, backgroundBitmap, painting, queue)
        eglDrawer!!.setBufferSize(width, height)
        updateTransform()
        eglDrawer!!.requestRender()
        if (painting.isPaused()) {
          painting.onResume()
        }
      }
      
      override fun onSurfaceTextureSizeChanged(
        surface: SurfaceTexture,
        width: Int,
        height: Int
      ) {
        val drawer = eglDrawer ?: return
        drawer.setBufferSize(width, height)
        updateTransform()
        drawer.requestRender()
      }
      
      override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        eglDrawer ?: return true
        if (!shuttingDown) {
          painting.onPause {
            eglDrawer!!.shutdown()
            eglDrawer = null
          }
        }
        return true
      }
      
      override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }
  }
  
  val isDrawAllowed: Boolean
    get() {
      return renderer.shouldDraw()
    }
  
  fun updateColor(value: Int) {
    currentColor = value
  }
  
  fun updateBrushSize(size: Float) {
    currentWeight = brushWeightForSize(size)
  }
  
  fun updateBrush(value: Brush) {
    currentBrush = value
    painting.setBrush(currentBrush)
  }
  
  fun onBeganDrawing() {
    renderer.onBeganDrawing()
  }
  
  fun onFinishedDrawing(moved: Boolean) {
    renderer.onFinishedDrawing(moved)
  }
  
  fun performInEGLContext(action: Action) {
    if (eglDrawer == null) {
      return
    }
    eglDrawer!!.postRunnable {
      eglDrawer!!.setCurrentContext()
      action()
    }
  }
  
  fun getResultBitmap(): Bitmap = eglDrawer!!.getTexture()
  
  fun shutdown() {
    shuttingDown = true
    if (eglDrawer != null) {
      performInEGLContext {
        painting.cleanResources(transformedBitmap)
        eglDrawer!!.shutdown()
        eglDrawer = null
      }
    }
  }
  
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val eglDrawer = eglDrawer ?: return false
    if (event.pointerCount > 1) {
      return false
    }
    if (eglDrawer.isInitialized && eglDrawer.isReady) {
      inputProcessor.process(event)
      return true
    }
    return false
  }
  
  private fun brushWeightForSize(size: Float): Float {
    val paintingWidth = painting.size.width
    return 8.0f / 2048.0f * paintingWidth + 90.0f / 2048.0f * paintingWidth * size
  }
  
  private fun updateTransform() {
    val matrix = Matrix()
    val paintingSize = painting.size
    val scale = width / paintingSize.width
    matrix.preTranslate(width / 2.0f, height / 2.0f)
    matrix.preScale(scale, -scale)
    matrix.preTranslate(-paintingSize.width / 2.0f, -paintingSize.height / 2.0f)
    inputProcessor.setMatrix(matrix)
    val projection = FloatArray(16)
    val right = eglDrawer!!.bufferWidth.toFloat()
    val top = eglDrawer!!.bufferHeight.toFloat()
    projection.orthoM(0.0f, right, 0.0f, top, -1.0f, 1.0f)
    val effectiveProjection = matrix.to4x4Matrix()
    val finalProjection = multiplyMatrices(projection,
      effectiveProjection)
    painting.setRenderProjection(finalProjection)
  }
}