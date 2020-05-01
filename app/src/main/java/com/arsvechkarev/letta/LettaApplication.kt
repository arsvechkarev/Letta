package com.arsvechkarev.letta

import android.app.Application
import android.content.res.Resources
import timber.log.Timber

class LettaApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
  
  companion object {
    
    var density: Float = -1f
      private set
    
    var scaledDensity: Float = -1f
      private set
    
    fun initResources(resources: Resources) {
      density = resources.displayMetrics.density
      scaledDensity = resources.displayMetrics.scaledDensity
    }
  }
}