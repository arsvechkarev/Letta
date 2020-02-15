package com.arsvechkarev.letta.editing

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.constants.DURATION_DEFAULT
import com.arsvechkarev.letta.utils.animateAlpha
import com.arsvechkarev.letta.utils.constraints
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.params
import com.arsvechkarev.letta.views.GradientPalette

class PaintContainer(private val buttonPaint: View, val rootView: View) {
  
  private var palette: GradientPalette = rootView.findViewById(R.id.palette)
  private var buttonBack: ImageButton = rootView.findViewById(R.id.buttonBack)
  
  init {
    palette.params {
      topMargin = buttonPaint.width * 2
      marginEnd = (buttonPaint.layoutParams as ViewGroup.MarginLayoutParams).marginEnd
    }
    rootView.post {
      ObjectAnimator.ofFloat(palette, View.X, palette.x + palette.width / 2,
        palette.x).setDuration(DURATION_DEFAULT).start()
      ObjectAnimator.ofFloat(buttonBack, View.X, -buttonBack.width.f / 2,
        buttonBack.x).setDuration(DURATION_DEFAULT).start()
      palette.animateAlpha(true, DURATION_DEFAULT)
      buttonBack.animateAlpha(true, DURATION_DEFAULT)
    }
  }
}