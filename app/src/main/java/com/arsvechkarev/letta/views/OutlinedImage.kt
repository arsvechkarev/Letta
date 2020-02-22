package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.i
import com.arsvechkarev.letta.utils.toBitmap

class OutlinedImage @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {
  
  private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.WHITE
    style = Paint.Style.STROKE
  }
  private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var imageSize = 0f
  private var innerPadding = 0f
  
  private val image: Bitmap?
  private val imageRect = RectF()
  
  init {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OutlinedImage, 0, 0)
    image = typedArray.getDrawable(R.styleable.OutlinedImage_image)?.toBitmap()
    innerPadding = typedArray.getDimension(R.styleable.OutlinedImage_innerPadding, 5.dp)
    strokePaint.strokeWidth = typedArray.getDimension(R.styleable.OutlinedImage_strokeWidth, 2.dp)
    typedArray.recycle()
    
    imageSize = image?.width?.f ?: 0f
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val size = imageSize + innerPadding * 2 + strokePaint.strokeWidth * 2 + paddingTop * 2
    setMeasuredDimension(resolveSize(size.i, widthMeasureSpec),
      resolveSize(size.i, widthMeasureSpec))
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    imageRect.set(innerPadding, innerPadding, w - innerPadding, h - innerPadding)
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(width / 2f, height / 2f, width / 2f - strokePaint.strokeWidth, strokePaint)
    if (image != null) canvas.drawBitmap(image, null, imageRect, imagePaint)
  }
  
}