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
  
  fun initialize() {
    val adapter = initializeAdapter()
    paletteBackground.onColorChangedByTouch = {
      adapter.disableSelection()
      backgroundImageExample.updateColor(it)
    }
    backgroundImageExample.updateDrawable(R.drawable.project_bg_1)
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
      ImageModel(R.drawable.project_bg_small_1, R.drawable.project_bg_1),
      ImageModel(R.drawable.project_bg_small_2, R.drawable.project_bg_2),
      ImageModel(R.drawable.project_bg_small_3, R.drawable.project_bg_3),
      ImageModel(R.drawable.project_bg_small_4, R.drawable.project_bg_4),
      ImageModel(R.drawable.project_bg_small_5, R.drawable.project_bg_5),
      ImageModel(R.drawable.project_bg_small_6, R.drawable.project_bg_6),
      ImageModel(R.drawable.project_bg_small_7, R.drawable.project_bg_7),
      ImageModel(R.drawable.project_bg_small_8, R.drawable.project_bg_8)
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
      else -> backgroundImageExample.updateDrawable(image.actualImageRes)
    }
  }
}