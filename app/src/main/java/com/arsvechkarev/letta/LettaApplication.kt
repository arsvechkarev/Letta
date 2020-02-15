package com.arsvechkarev.letta

import android.app.Application

class LettaApplication : Application(){
  
  companion object {
    var density: Float = -0f
  }
  
  override fun onCreate() {
    super.onCreate()
    density = resources.displayMetrics.density
  }
  
}