package com.arsvechkarev.letta.editing

import android.view.View
import androidx.annotation.IdRes
import com.arsvechkarev.letta.utils.toggleKeyboard
import com.arsvechkarev.letta.views.ListenableConstraintLayout

abstract class Container(
  val view: ListenableConstraintLayout,
  private val animateOnAttach: Boolean = true
) :
  ListenableConstraintLayout.Listener {
  
  init {
    view.listener = this
  }
  
  protected fun post(block: () -> Unit) {
    view.post(block)
  }
  
  protected fun <T : View> findViewById(@IdRes id: Int): T {
    return view.findViewById(id)
  }
  
  protected fun toggleKeyboard() {
    view.toggleKeyboard()
  }
  
  override fun onAttachedToWindow() {
    if (animateOnAttach) {
      animateEnter()
    }
  }
  
  open fun onBackPressed() {}
  
  open fun animateEnter() {}
  open fun animateExit(andThen: () -> Unit) {
    andThen()
  }
}