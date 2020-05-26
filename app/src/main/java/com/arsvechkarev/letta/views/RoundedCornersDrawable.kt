package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.COLOR_BORDER_LIGHT
import com.arsvechkarev.letta.utils.dimen
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.toBitmap

open class RoundedCornersDrawable(
  private val image: Bitmap,
  borderColor: Int,
  private val borderWidth: Float,
  private val cornersRadius: Float
) : Drawable() {
  
  private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    color = borderColor
    strokeWidth = borderWidth
  }
  private val borderRect = RectF()
  
  private val imageMatrix = Matrix()
  private val imageRect = RectF()
  
  protected val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  
  override fun onBoundsChange(bounds: Rect) {
    val tempRect = RectF(
      bounds.left + borderWidth,
      bounds.top + borderWidth,
      bounds.right - borderWidth,
      bounds.bottom - borderWidth
    )
    imageRect.set(tempRect)
    tempRect.inset(borderWidth / 2f, borderWidth / 2f)
    borderRect.set(tempRect)
    updateMatrix(this.image, bounds.width(), bounds.height())
  }
  
  override fun draw(canvas: Canvas) {
    canvas.drawRoundRect(imageRect, cornersRadius, cornersRadius, imagePaint)
    
    imagePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    canvas.drawBitmap(image, imageMatrix, imagePaint)
    
    canvas.drawRoundRect(borderRect, cornersRadius, cornersRadius, borderPaint)
  }
  
  override fun setAlpha(alpha: Int) {
    imagePaint.alpha = alpha
  }
  
  override fun getOpacity(): Int = PixelFormat.OPAQUE
  
  override fun setColorFilter(colorFilter: ColorFilter?) {
    imagePaint.colorFilter = colorFilter
  }
  
  private fun updateMatrix(image: Bitmap, width: Int, height: Int) {
    val scale: Float
    var dy = 0f
    var dx = 0f
    if (image.width * height > image.height * width) {
      scale = height.f / image.height
      dx = (width - image.width * scale) * 0.5f
    } else {
      scale = width.toFloat() / image.width
      dy = (height - image.height * scale) * 0.5f
    }
    imageMatrix.setScale(scale, scale)
    imageMatrix.postTranslate(dx, dy)
  }
  
  companion object {
    
    fun ofBitmap(context: Context, bitmap: Bitmap): RoundedCornersDrawable {
      return RoundedCornersDrawable(bitmap, COLOR_BORDER_LIGHT, context.dimen(R.dimen.border_width),
        context.dimen(R.dimen.corners_radius_default))
    }
    
    fun ofResource(
      context: Context,
      resId: Int,
      corners: Float = context.dimen(R.dimen.corners_radius_default)
    ): RoundedCornersDrawable {
      return RoundedCornersDrawable(
        context.getDrawable(resId)!!.toBitmap(),
        COLOR_BORDER_LIGHT,
        context.dimen(R.dimen.border_width),
        corners
      )
    }
  }
}