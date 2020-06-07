package com.arsvechkarev.letta.core.brushes

sealed class BackgroundType {
  
  class Color(val color: Int) : BackgroundType()
  
  class DrawableRes(val drawableRes: Int) : BackgroundType()
}