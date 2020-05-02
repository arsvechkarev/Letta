package com.arsvechkarev.letta.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.KeyEvent.ACTION_UP
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import com.arsvechkarev.letta.core.brushes.Brush
import com.arsvechkarev.letta.core.brushes.BrushFactory
import com.arsvechkarev.letta.core.brushes.BrushType

class DrawingView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val records = ArrayList<Brush>()
  private var latestX = 0f
  private var latestY = 0f
  
  var brushType: BrushType = BrushType.CIRCLE
  private lateinit var brush: Brush
  
  var brushColor = 0
  
  var brushSize = 0f
    set(value) {
      if (value > 0f)
        field = value
    }
  
  var onDown: () -> Unit = {}
  var onUp: () -> Unit = {}
  
  fun clear() {
    records.clear()
    invalidate()
  }
  
  fun undo() {
    if (records.size > 0) {
      records.removeAt(records.size - 1)
      invalidate()
    }
  }
  
  override fun onDraw(canvas: Canvas) {
    records.forEach { it.draw(canvas) }
  }
  
  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (!isClickable) return false
    when (event.action) {
      ACTION_DOWN -> {
        onDown()
        brush = createNewBrush()
        brush.onDown(event.x, event.y)
        latestX = event.x
        latestY = event.y
        invalidate()
        return true
      }
      ACTION_MOVE -> {
        brush.onMove(latestX, latestY, event.x, event.y)
        latestX = event.x
        latestY = event.y
        invalidate()
        return true
      }
      ACTION_UP -> {
        onUp()
        brush.onUp(event.x, event.y)
        invalidate()
        return true
      }
      else -> return false
    }
  }
  
  private fun createNewBrush(): Brush {
    brush = BrushFactory.createBrush(brushType, brushColor, brushSize)
    brush.brushSize = brushSize
    brush.color = brushColor
    records.add(brush)
    return brush
  }
}