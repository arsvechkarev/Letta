package com.arsvechkarev.letta.features.drawing

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.Image
import com.arsvechkarev.letta.features.drawing.list.BackgroundImageAdapter
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

class ChooseBackgroundContainer(
  backgroundImageView: ImageView,
  backgroundColorView: View,
  palette: GradientPalette,
  backgroundImageRecyclerView: RecyclerView
) {
  
  init {
    val images = listOf(
      Image(R.drawable.bg_0_for_png),
      Image(R.drawable.bg_1),
      Image(R.drawable.bg_2),
      Image(R.drawable.bg_3)
    )
    backgroundImageRecyclerView.adapter = BackgroundImageAdapter(images)
    backgroundImageRecyclerView.layoutManager = LinearLayoutManager(backgroundImageView.context,
      LinearLayoutManager.HORIZONTAL, false)
    palette.onColorChanged = {
      backgroundImageView.setImageDrawable(ColorDrawable(it))
    }
  }
}