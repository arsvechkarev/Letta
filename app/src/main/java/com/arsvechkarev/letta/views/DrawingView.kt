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
import com.arsvechkarev.letta.core.brushes.SprayBrush

class DrawingView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private val records = ArrayList<Brush>()
  private var latestX = 0f
  private var latestY = 0f
  
  lateinit var currentBrush: Brush
  
  var brushColor = 0
  
  var brushWidth = 0f
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
        currentBrush = createNewBrush()
        currentBrush.onDown(event.x, event.y)
        latestX = event.x
        latestY = event.y
        invalidate()
        return true
      }
      ACTION_MOVE -> {
        currentBrush.onMove(latestX, latestY, event.x, event.y)
        latestX = event.x
        latestY = event.y
        invalidate()
        return true
      }
      ACTION_UP -> {
        onUp()
        currentBrush.onUp(event.x, event.y)
        invalidate()
        return true
      }
      else -> return false
    }
  }
  
  private fun createNewBrush(): Brush {
    currentBrush = SprayBrush(brushColor, brushWidth)
    records.add(currentBrush)
    return currentBrush
  }
}