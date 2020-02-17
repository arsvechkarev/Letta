package com.arsvechkarev.letta.utils

import android.view.View
import com.arsvechkarev.letta.animations.animateToolAppearing
import com.arsvechkarev.letta.animations.animateToolHiding

class Group(vararg views: View) {
  
  private val viewsList = views.asList()
  
  fun animateHide(except: View) {
    viewsList.forEach {
      if (it != except) it.animateToolHiding()
    }
  }
  
  fun animateAppear(except: View) {
    viewsList.forEach {
      if (it != except) it.animateToolAppearing()
    }
  }
  
}