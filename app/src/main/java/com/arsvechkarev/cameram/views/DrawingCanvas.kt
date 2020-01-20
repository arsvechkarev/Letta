package com.arsvechkarev.cameram.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.graphics.Path
import android.os.Environment.getExternalStorageDirectory
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent.ACTION_UP
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
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
  
  private fun debug(message: String) = Log.d("DrawingCanvas", message)
  private fun debug(e: Exception) {
    Log.e("DrawingCanvas", "Error: ", e)
  }
  
  private var latestPath = Path()
  private var latestPaint = Paint(ANTI_ALIAS_FLAG and DITHER_FLAG)
  private var lastX = 0f
  private var lastY = 0f
  private var currentPaintWidth =
    PAINT_WIDTH_COEFFICIENT
  private var currentPaintColor =
    INITIAL_PAINT_COLOR
  
  private val paths = ArrayList<Path>()
  private val paints = ArrayList<Paint>()
  
  init {
    setBackgroundColor(INITIAL_BG_COLOR)
    latestPaint = createNewPaint()
  }
  
  var onDown: (Int, Int) -> Unit = { _, _ -> }
  var onMove: (Int, Int) -> Unit = { _, _ -> }
  var onUp: (Int, Int) -> Unit = { _, _ -> }
  var onLast: (Int, Int) -> Unit = { _, _ -> }
  
  fun changeWidth(width: Int) {
    if (width > 0) {
      currentPaintWidth = width * PAINT_WIDTH_COEFFICIENT
    }
  }
  
  fun setPaintColor(@ColorInt color: Int) {
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
    val bitmap = background.toBitmap(width, height)
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
        debug("Image saved successfully")
      } catch (e: Exception) {
        debug(e)
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
    println("paints = ${paints.size}")
    println("paths = ${paths.size}")
    paints.forEachIndexed { i, paint ->
      canvas.drawPath(paths[i], paint)
    }
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        onDown(event.x.toInt(), event.y.toInt())
        onLast(event.x.toInt(), event.y.toInt())
        handleTouchDown(event.x, event.y)
        invalidate()
        return true
      }
      ACTION_MOVE -> {
        onMove(event.x.toInt(), event.y.toInt())
        onLast(event.x.toInt(), event.y.toInt())
        handleTouchMove(event.x, event.y)
        invalidate()
        return true
      }
      ACTION_UP -> {
        onUp(event.x.toInt(), event.y.toInt())
        onLast(event.x.toInt(), event.y.toInt())
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
    private const val PAINT_WIDTH_COEFFICIENT = 3f
    private const val TOUCH_TOLERANCE = 6f
    
    private const val INITIAL_PAINT_COLOR = Color.RED
    private const val INITIAL_BG_COLOR = Color.BLACK
  }
  
}