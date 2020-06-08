package com.arsvechkarev.letta.core.model

sealed class BackgroundType {
  
  class Color(val color: Int) : BackgroundType()
  
  class DrawableRes(val drawableRes: Int) : BackgroundType()
}