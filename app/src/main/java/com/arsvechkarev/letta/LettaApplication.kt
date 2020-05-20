package com.arsvechkarev.letta

import android.app.Application
import android.content.Context
import android.content.res.Resources
import timber.log.Timber

class LettaApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    appContext = applicationContext
  }
  
  companion object {
    
    lateinit var resources: Resources
    lateinit var appContext: Context
    
    var density: Float = -1f
      private set
    
    var scaledDensity: Float = -1f
      private set
    
    // Should be called from the activity that launches first, otherwise
    // the resources will not be ready
    fun initResources(resources: Resources) {
      this.resources = resources
      density = resources.displayMetrics.density
      scaledDensity = resources.displayMetrics.scaledDensity
    }
  }
}