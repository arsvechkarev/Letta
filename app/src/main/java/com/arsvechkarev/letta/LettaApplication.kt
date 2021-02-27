package com.arsvechkarev.letta

import android.app.Application
import android.content.Context
import com.arsvechkarev.opengldrawing.ContextHolder
import timber.log.Timber

class LettaApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    ContextHolder.setApplicationContext(applicationContext)
    appContext = applicationContext
  }
  
  companion object {
    
    lateinit var appContext: Context
      private set
    
    var density: Float = -1f
      private set
    
    var scaledDensity: Float = -1f
      private set
    
    // Should be called from the activity that launches first, otherwise
    // densities will not be ready
    fun initDensities(density: Float, scaledDensity: Float) {
      this.density = density
      this.scaledDensity = scaledDensity
    }
  }
}