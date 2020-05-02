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
    
    lateinit var resources: Resources
    
    var density: Float = -1f
      private set
    
    var scaledDensity: Float = -1f
      private set
    
    fun initResources(resources: Resources) {
      this.resources = resources
      density = resources.displayMetrics.density
      scaledDensity = resources.displayMetrics.scaledDensity
    }
  }
}