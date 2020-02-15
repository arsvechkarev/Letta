package com.arsvechkarev.letta.utils

import android.view.View
import com.arsvechkarev.letta.editing.animateToolHiding

class Group(vararg views: View) {
  
  private val viewsList = views.asList()
  
  fun animateHide(except: View) {
    viewsList.forEach {
      if (it != except) { it.animateToolHiding() }
    }
  }
  
  fun animateAlpha(visible: Boolean, duration: Long) {
    viewsList.forEach {
      it.animateAlpha(visible, duration)
    }
  }
  
}