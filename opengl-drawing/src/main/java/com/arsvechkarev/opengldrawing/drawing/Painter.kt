package com.arsvechkarev.opengldrawing.drawing

import android.graphics.RectF
import com.arsvechkarev.opengldrawing.UndoStore

interface Painter {
  val undoStore: UndoStore
  fun onContentChanged(rect: RectF?) {}
}
  