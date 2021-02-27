package com.arsvechkarev.opengldrawing

import java.util.ArrayList

class UndoStore(private val onHistoryChanged: Action = {}) {
  
  private val operations = ArrayList<Action>()
  
  val isNotEmpty get() = operations.isNotEmpty()
  
  val isEmpty get() = operations.isEmpty()
  
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
    Threader.onMainThread { onHistoryChanged.invoke() }
  }
}