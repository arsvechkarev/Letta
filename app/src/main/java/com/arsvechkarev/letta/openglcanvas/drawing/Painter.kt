package com.arsvechkarev.letta.openglcanvas.drawing

import android.graphics.RectF
import com.arsvechkarev.letta.openglcanvas.UndoStore

interface Painter {
  val undoStore: UndoStore
  fun onContentChanged(rect: RectF?) {}
}
  