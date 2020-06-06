package com.arsvechkarev.letta.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.COLOR_BORDER_LIGHT
import com.arsvechkarev.letta.extensions.f
import com.arsvechkarev.letta.extensions.getDimen
import com.arsvechkarev.letta.extensions.toBitmap

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
  
  private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  
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
    val bitmapShader = BitmapShader(this.image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    updateMatrix(this.image, bounds.width(), bounds.height())
    bitmapShader.setLocalMatrix(imageMatrix)
    imagePaint.shader = bitmapShader
  }
  
  override fun draw(canvas: Canvas) {
    canvas.drawRoundRect(imageRect, cornersRadius, cornersRadius, imagePaint)
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
      return RoundedCornersDrawable(bitmap, COLOR_BORDER_LIGHT, context.getDimen(R.dimen.border_width),
        context.getDimen(R.dimen.corners_radius_default))
    }
    
    fun ofResource(
      context: Context,
      resId: Int,
      corners: Float = context.getDimen(R.dimen.corners_radius_default)
    ): RoundedCornersDrawable {
      return RoundedCornersDrawable(
        context.getDrawable(resId)!!.toBitmap(),
        COLOR_BORDER_LIGHT,
        context.getDimen(R.dimen.border_width),
        corners
      )
    }
  }
}