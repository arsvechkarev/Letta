package com.arsvechkarev.letta.openglcanvas

import com.arsvechkarev.letta.core.async.AndroidThreader.onMainThread
import java.util.ArrayList

class UndoStore(private val onHistoryChanged: Action = {}) {
  
  private val operations = ArrayList<Action>()
  
  val canUndo get() = operations.isNotEmpty()
  
  fun registerUndo(action: Action) {
    operations.add(action)
    notifyOfHistoryChanges()
  }
  
  fun undo() {
    if (operations.size == 0) {
      return
    }
    val lastIndex = operations.size - 1
    operations[lastIndex].invoke()
    operations.removeAt(lastIndex)
    notifyOfHistoryChanges()
  }
  
  private fun notifyOfHistoryChanges() {
    onMainThread { onHistoryChanged.invoke() }
  }
}