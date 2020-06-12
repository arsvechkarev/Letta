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
import com.arsvechkarev.letta.core.DURATION_MEDIUM
import com.arsvechkarev.letta.extensions.getDimen

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
    val verticalMargin = context.getDimen(R.dimen.clickable_text_view_margin).toInt()
    val horizontalMargin = (verticalMargin * 1.8f).toInt()
    setPadding(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin)
  }
  
  private fun setRipple(rippleColor: Int) {
    val r = context.getDimen(R.dimen.corners_radius_default)
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
    postDelayed({ isClickable = true }, DURATION_MEDIUM)
    return super.performClick()
  }
}