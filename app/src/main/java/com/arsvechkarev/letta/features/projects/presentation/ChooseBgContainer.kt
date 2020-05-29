package com.arsvechkarev.letta.features.projects.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.ImageModel
import com.arsvechkarev.letta.features.projects.list.BackgroundImageAdapter
import com.arsvechkarev.letta.views.BorderImageView
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

class ChooseBgContainer(
  private val backgroundImageExample: BorderImageView,
  private val paletteBackground: GradientPalette,
  private val backgroundImagesRecyclerView: RecyclerView
) {
  
  init {
    val adapter = initializeAdapter()
    paletteBackground.onColorChanged = {
      adapter.disableSelection()
      backgroundImageExample.updateColor(it)
    }
    backgroundImageExample.updateDrawable(R.drawable.bg_1)
  }
  
  private fun initializeAdapter(): BackgroundImageAdapter {
    val images = listOf(
      ImageModel(R.drawable.bg_1),
      ImageModel(R.drawable.bg_2),
      ImageModel(R.drawable.bg_3)
    )
    val adapter = BackgroundImageAdapter(
      images, ::onImageSelected)
    backgroundImagesRecyclerView.adapter = adapter
    backgroundImagesRecyclerView.layoutManager = LinearLayoutManager(backgroundImageExample.context,
      LinearLayoutManager.HORIZONTAL, false)
    return adapter
  }
  
  
  private fun onImageSelected(image: ImageModel?) {
    when (image) {
      null -> backgroundImageExample.updateColor(paletteBackground.currentColor)
      else -> backgroundImageExample.updateDrawable(image.drawableRes)
    }
  }
}