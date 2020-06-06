package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.RectF
import com.arsvechkarev.letta.opengldrawing.UndoStore

interface Painter {
  val undoStore: UndoStore
  fun onContentChanged(rect: RectF?) {}
}
  