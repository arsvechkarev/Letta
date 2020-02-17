package com.arsvechkarev.letta.editing

import android.view.View
import androidx.annotation.IdRes

abstract class Container(val view: View) {
  
  fun <T : View> findViewById(@IdRes id: Int): T {
    return view.findViewById(id)
  }
  
  open fun animateEnter() {}
  open fun animateExit(andThen: () -> Unit) {}
}