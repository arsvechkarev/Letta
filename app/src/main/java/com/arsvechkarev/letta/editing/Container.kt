package com.arsvechkarev.letta.editing

import android.view.View
import androidx.annotation.IdRes
import com.arsvechkarev.letta.utils.toggleKeyboard

abstract class Container(val view: View) {
  
  protected fun post(block: () -> Unit) {
    view.post(block)
  }
  
  protected fun <T : View> findViewById(@IdRes id: Int): T {
    return view.findViewById(id)
  }
  
  protected fun toggleKeyboard() {
    view.toggleKeyboard()
  }
  
  open fun animateEnter() {}
  open fun animateExit(andThen: () -> Unit) {
    andThen()
  }
}