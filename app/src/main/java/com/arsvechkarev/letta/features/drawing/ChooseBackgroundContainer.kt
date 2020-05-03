package com.arsvechkarev.letta.features.drawing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.ImageModel
import com.arsvechkarev.letta.features.drawing.list.BackgroundImageAdapter
import com.arsvechkarev.letta.core.COLOR_BORDER_LIGHT
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.views.Image
import com.arsvechkarev.letta.views.RoundedCornersColorDrawable
import com.arsvechkarev.letta.views.RoundedCornersDrawable
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette

class ChooseBackgroundContainer(
  private val backgroundImage: ImageView,
  private val backgroundColorView: View,
  private val currentBackgroundImage: Image,
  private val gradientPalette: GradientPalette,
  private val backgroundImageRecyclerView: RecyclerView
) {
  
  private val currentBgCorners = 4.dp
  
  // To avoid creation of drawables
  private var roundedCornersColorDrawable: RoundedCornersColorDrawable? = null
  
  init {
    val adapter = initializeAdapter()
    gradientPalette.onColorChanged = {
      adapter.disableSelection()
      backgroundColorView.setBackgroundColor(it)
      updateCurrentBackgroundColor()
    }
    currentBackgroundImage.setImage(RoundedCornersDrawable.ofResource(
      currentBackgroundImage.context, R.drawable.bg_0_for_png, currentBgCorners
    ))
  }
  
  private fun initializeAdapter(): BackgroundImageAdapter {
    val images = listOf(
      ImageModel(R.drawable.bg_0_for_png),
      ImageModel(R.drawable.bg_1),
      ImageModel(R.drawable.bg_2),
      ImageModel(R.drawable.bg_3)
    )
    val adapter = BackgroundImageAdapter(images, ::onImageSelected)
    backgroundImageRecyclerView.adapter = adapter
    backgroundImageRecyclerView.layoutManager = LinearLayoutManager(backgroundImage.context,
      LinearLayoutManager.HORIZONTAL, false)
    return adapter
  }
  
  private fun onImageSelected(image: ImageModel?) {
    when (image) {
      null -> {
        // Deselecting current image
        updateCurrentBackgroundColor()
      }
      else -> {
        val drawable = RoundedCornersDrawable.ofResource(
          backgroundImage.context, image.drawableRes, currentBgCorners
        )
        backgroundImage.setImageResource(image.drawableRes)
        backgroundColorView.setBackgroundColor(0)
        currentBackgroundImage.setImage(drawable)
      }
    }
  }
  
  private fun updateCurrentBackgroundColor() {
    backgroundImage.setImageDrawable(null)
    backgroundColorView.setBackgroundColor(gradientPalette.currentColor)
    currentBackgroundImage.setImage(
      createOrUseColorDrawable(gradientPalette.currentColor)
    )
  }
  
  private fun createOrUseColorDrawable(color: Int): RoundedCornersColorDrawable {
    if (roundedCornersColorDrawable == null) {
      val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bitmap)
      canvas.drawColor(color)
      roundedCornersColorDrawable = RoundedCornersColorDrawable(bitmap,
        COLOR_BORDER_LIGHT, 1.dp,
        currentBgCorners)
    }
    roundedCornersColorDrawable!!.setColor(color)
    return roundedCornersColorDrawable!!
  }
}
