package com.arsvechkarev.letta.editing

import android.view.View
import android.view.View.TRANSLATION_X
import android.widget.ImageButton
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.animations.animateFadeIn
import com.arsvechkarev.letta.animations.animateFadeOut
import com.arsvechkarev.letta.utils.animate
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.views.GradientPalette

class PaintContainer(view: View) : Container(view) {
  
  private var palette: GradientPalette = findViewById(R.id.palette)
  private var buttonBack: ImageButton = findViewById(R.id.buttonBack)
  
  override fun animateEnter() {
    view.post {
      buttonBack.animateFadeIn()
      buttonBack.animate(TRANSLATION_X, -buttonBack.width / 2f, 0f)
      palette.animateFadeIn()
      palette.animate(TRANSLATION_X, palette.width / 2f, 0f)
    }
  }
  
  override fun animateExit(andThen: () -> Unit) {
    buttonBack.animateFadeOut(andThen)
    buttonBack.animate(TRANSLATION_X, -buttonBack.width / 2f)
    palette.animateFadeOut(andThen)
    palette.animate(TRANSLATION_X, palette.width / 2f, onEnd = {
      andThen()
    })
  }
}