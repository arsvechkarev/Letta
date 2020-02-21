package com.arsvechkarev.letta

import android.app.Application
import android.content.res.Resources

class LettaApplication : Application() {
  
  companion object {
    fun initResources(resources: Resources) {
      density = resources.displayMetrics.density
    }
    
    var density: Float = -1f
      private set
  }
  
}