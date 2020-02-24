package com.arsvechkarev.letta.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.BoringLayout
import android.text.Layout
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.TextVariant

class TextVariantView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  private var textVariant: TextVariant? = null
  private var textSize = resources.getDimension(R.dimen.text_variant_default)
  private var textLayout: Layout? = null
  
  fun draw(textVariant: TextVariant) {
    this.textVariant = textVariant
    this.textVariant!!.paint.textSize = textSize
    invalidate()
  }
  
  @SuppressLint("DrawAllocation")
  @Suppress("DEPRECATION")
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (textVariant == null) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
      return
    }
    textVariant!!.let {
      val textWidth = it.paint.measureText(it.text.toString()).toInt()
      val metrics = BoringLayout.isBoring(it.text, it.paint)
      textLayout = BoringLayout(
        it.text, it.paint, textWidth, Layout.Alignment.ALIGN_NORMAL,
        1f, 0f, metrics, false
      )
      setMeasuredDimension(
        resolveSize(textWidth, widthMeasureSpec),
        resolveSize(textLayout!!.height, heightMeasureSpec)
      )
    }
  }
  
  override fun onDraw(canvas: Canvas) {
    textVariant?.let { textLayout!!.draw(canvas) }
    drawBounds(canvas)
  }
  
}