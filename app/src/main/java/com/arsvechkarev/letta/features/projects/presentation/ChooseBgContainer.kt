package com.arsvechkarev.letta.features.projects.presentation

import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.BackgroundType
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
    paletteBackground.onColorChangedByTouch = {
      adapter.disableSelection()
      backgroundImageExample.updateColor(it)
    }
    backgroundImageExample.updateDrawable(R.drawable.bg1)
  }
  
  fun getBackgroundType(): BackgroundType {
    if (backgroundImageExample.resId == 0) {
      val color = (backgroundImageExample.background as ColorDrawable).color
      return BackgroundType.Color(color)
    }
    return BackgroundType.DrawableRes(backgroundImageExample.resId)
  }
  
  private fun initializeAdapter(): BackgroundImageAdapter {
    val images = listOf(
      ImageModel(R.drawable.bg1),
      ImageModel(R.drawable.bg2),
      ImageModel(R.drawable.bg3),
      ImageModel(R.drawable.bg4),
      ImageModel(R.drawable.bg5),
      ImageModel(R.drawable.bg6),
      ImageModel(R.drawable.bg7),
      ImageModel(R.drawable.bg8)
    )
    val adapter = BackgroundImageAdapter(images, ::onImageSelected)
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