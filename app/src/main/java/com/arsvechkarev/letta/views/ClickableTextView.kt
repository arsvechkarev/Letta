package com.arsvechkarev.letta.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.DURATION_LONG
import com.arsvechkarev.letta.extensions.dp
import com.arsvechkarev.letta.extensions.dpInt

class ClickableTextView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
  
  init {
    val attributes = context.obtainStyledAttributes(attrs, R.styleable.ClickableTextView,
      defStyleAttr, 0)
    val rippleColor = attributes.getColor(R.styleable.ClickableTextView_rippleColor, Color.WHITE)
    setRipple(rippleColor)
    attributes.recycle()
    isClickable = true
    isFocusable = true
    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    setPadding(16.dpInt, 8.dpInt, 16.dpInt, 8.dpInt)
  }
  
  private fun setRipple(rippleColor: Int) {
    val r = 4.dp
    val roundRectShape = RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null)
    val backgroundRect = ShapeDrawable().apply {
      shape = roundRectShape
      paint.color = Color.TRANSPARENT
    }
    val maskRect = ShapeDrawable().apply {
      shape = roundRectShape
      paint.color = rippleColor
    }
    background = RippleDrawable(ColorStateList.valueOf(rippleColor), backgroundRect, maskRect)
  }
  
  override fun performClick(): Boolean {
    isClickable = false
    postDelayed({ isClickable = true }, DURATION_LONG)
    return super.performClick()
  }
}