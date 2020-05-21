package com.arsvechkarev.letta.core.brushes

import com.arsvechkarev.letta.LettaApplication
import com.arsvechkarev.letta.core.brushes.BrushType.CIRCLE
import com.arsvechkarev.letta.core.brushes.BrushType.OVAL
import com.arsvechkarev.letta.core.brushes.BrushType.SPRAY

object BrushFactory {
  
  fun createBrush(brushType: BrushType, color: Int, brushSize: Float): Brush {
    return when (brushType) {
      CIRCLE -> CircleBrush(color, brushSize)
      SPRAY -> SprayBrush(color, brushSize)
      OVAL -> OvalBrush(LettaApplication.appContext.resources, color, brushSize)
    }
  }
}