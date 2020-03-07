package com.arsvechkarev.letta.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Environment.getExternalStorageDirectory
import android.util.AttributeSet
import android.view.KeyEvent.ACTION_UP
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import androidx.annotation.ColorInt
import com.arsvechkarev.letta.utils.extenstions.toBitmap
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.abs


class DrawingCanvas @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private var latestPath = Path()
  private var latestPaint = Paint(ANTI_ALIAS_FLAG and DITHER_FLAG)
  private var lastX = 0f
  private var lastY = 0f
  private var currentPaintWidth = 0f
  private var currentPaintColor = INITIAL_PAINT_COLOR
  private val paths = ArrayList<Path>()
  
  private val paints = ArrayList<Paint>()
  private val bitmaps = ArrayList<Bitmap>()
  
  var isEraserMode: Boolean = false
  
  init {
    setBackgroundColor(Color.TRANSPARENT)
    latestPaint = createNewPaint()
  }
  
  var onDown: () -> Unit = {}
  var onUp: () -> Unit = {}
  
  fun setPaintWidth(width: Float) {
    if (width > 0) {
      currentPaintWidth = width
    }
  }
  
  fun setPaintColor(@ColorInt color: Int) {
    isEraserMode = false
    currentPaintColor = color
  }
  
  fun clear() {
    paths.clear()
    paints.clear()
    latestPaint = createNewPaint()
    latestPath = Path()
    invalidate()
  }
  
  @Suppress("DEPRECATION")
  fun saveBitmapToGallery() {
    val bitmap = background.toBitmap()
    val canvas = Canvas(bitmap)
    for (i in paths.indices) {
      canvas.drawPath(paths[i], paints[i])
    }
    val baseDir = getExternalStorageDirectory()
    val directory = File(baseDir, "${separator}DrawerCanvas${separator}Pictures")
    thread {
      try {
        if (!directory.exists()) {
          directory.mkdirs()
        }
        val dateFormat = SimpleDateFormat("yyyy_MM_dd-HH_mm_ss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        val newFile = File(directory, "image_$timestamp.jpg")
        FileOutputStream(newFile).use {
          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }
      } catch (e: Exception) {
      }
    }
    
  }
  
  fun undo() {
    if (paths.size > 0) {
      paths.removeAt(paths.size - 1)
      paints.removeAt(paints.size - 1)
      invalidate()
    }
  }
  
  override fun onDraw(canvas: Canvas) {
    paints.forEachIndexed { i, paint ->
      canvas.drawPath(paths[i], paint)
    }
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (!isClickable) return false
    when (event.action) {
      ACTION_DOWN -> {
        onDown()
        handleTouchDown(event.x, event.y)
        invalidate()
        return true
      }
      ACTION_MOVE -> {
        handleTouchMove(event.x, event.y)
        invalidate()
        return true
      }
      ACTION_UP -> {
        onUp()
        handleTouchUp()
        invalidate()
        return true
      }
      else -> return false
    }
  }
  
  private fun createNewPaint() = Paint(ANTI_ALIAS_FLAG).apply {
    color = currentPaintColor
    style = Paint.Style.STROKE
    strokeJoin = Paint.Join.ROUND
    strokeCap = Paint.Cap.ROUND
    strokeWidth = currentPaintWidth
    if (isEraserMode) {
      color = Color.TRANSPARENT
      xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }
  }
  
  private fun handleTouchDown(x: Float, y: Float) {
    latestPaint = createNewPaint()
    latestPath = Path()
    paints.add(latestPaint)
    paths.add(latestPath)
    latestPath.moveTo(x, y)
    latestPath.lineTo(x, y)
    lastX = x
    lastY = y
  }
  
  private fun handleTouchMove(x: Float, y: Float) {
    val dx = abs(x - lastX)
    val dy = abs(y - lastY)
    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
      latestPath.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2)
      lastX = x
      lastY = y
    }
  }
  
  private fun handleTouchUp() {
    latestPath.lineTo(lastX, lastY)
  }
  
  companion object {
    private const val TOUCH_TOLERANCE = 6f
    
    private const val INITIAL_PAINT_COLOR = Color.RED
  }
  
}