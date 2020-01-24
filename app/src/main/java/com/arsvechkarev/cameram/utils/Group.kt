package com.arsvechkarev.cameram.utils

import android.view.View

class Group(vararg views: View) {
  
  private val viewsList = views.asList()
  
  fun animateAlpha(visible: Boolean, duration: Long) {
    viewsList.forEach {
      it.animateAlpha(visible, duration)
    }
  }
  
}