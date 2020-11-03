package com.arsvechkarev.letta.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.DURATION_DEFAULT
import com.arsvechkarev.letta.core.DURATION_SHORT
import com.arsvechkarev.letta.extensions.AccelerateDecelerateInterpolator
import com.arsvechkarev.letta.extensions.execute
import com.arsvechkarev.letta.extensions.toBitmap

class HideToolsView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ImageButton(context, attrs, defStyleAttr) {
  
  private var mirrored = false
  private var rotationCoeff = 1f
  private val drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_right)!!
  private val bitmapMatrix = Matrix()
  private lateinit var bitmap: Bitmap
  
  private val animator = ValueAnimator().apply {
    duration = DURATION_DEFAULT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      rotationCoeff = it.animatedValue as Float
      bitmapMatrix.setScale(rotationCoeff, 1f, width / 2f, height / 2f)
      invalidate()
    }
  }
  
  fun makeInvisible() {
    animate().alpha(0f)
        .setDuration(DURATION_SHORT)
        .start()
  }
  
  fun makeVisible() {
    animate().alpha(1f)
        .setDuration(DURATION_SHORT)
        .start()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    drawable.setBounds(paddingLeft, paddingTop, w - paddingRight, h - paddingBottom)
    bitmap = drawable.toBitmap(w, h)
  }
  
  override fun performClick(): Boolean {
    val endValue = if (mirrored) 1f else -1f
    animator.setFloatValues(rotationCoeff, endValue)
    animator.start()
    mirrored = !mirrored
    return super.performClick()
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.execute {
      super.onDraw(canvas)
      drawBitmap(bitmap, bitmapMatrix, null)
    }
  }
  
}