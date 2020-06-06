package com.arsvechkarev.letta.core.brushes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.arsvechkarev.letta.extensions.dp
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.extensions.i
import kotlin.random.Random

class CircleBrush(color: Int, brushSize: Float) : Brush(color, brushSize) {
  
  private val random = Random
  private val pointSize = 1.dp
  private val exampleRect = RectF()
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    this.color = color
  }
  
  private val sprayBitmap =
      Bitmap.createBitmap(brushSize.i, brushSize.i, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        drawSpray(canvas, brushSize / 2, brushSize / 2)
      }
  private val points = ArrayList<Float>()
  
  override val type = BrushType.CIRCLE
  
  override fun onDown(x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
  
  override fun onMove(lastX: Float, lastY: Float, x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
  
  override fun onUp(x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
  
  override fun draw(canvas: Canvas) {
    var i = 0
    while (i < points.size) {
      val x = points[i]
      val y = points[i + 1]
      i += 2
      canvas.drawBitmap(sprayBitmap, x - brushSize / 2, y - brushSize / 2, paint)
    }
  }
  
  override fun onExampleDraw(canvas: Canvas, x: Float, y: Float, brushSize: Float) {
    exampleRect.set(
      x - brushSize / 2f,
      y - brushSize / 2f,
      x + brushSize / 2f,
      y + brushSize / 2f
    )
    canvas.drawBitmap(sprayBitmap, null, exampleRect, paint)
  }
  
  private fun drawSpray(canvas: Canvas, x: Float, y: Float) {
    val totalPoints = random.nextInt((brushSize * 0.4f).i, (brushSize * 0.6f).i)
    val path = Path()
    path.addCircle(x, y, brushSize / 2f, Path.Direction.CW)
    canvas.clipPath(path)
    for (i in 0..totalPoints) {
      val randomX = random.nextInt((x - brushSize / 2).i, (x + brushSize / 2).i).f
      val randomY = random.nextInt((y - brushSize / 2).i, (y + brushSize / 2).i).f
      canvas.drawRect(randomX, randomY, randomX + pointSize, randomY + pointSize, paint)
    }
  }
}