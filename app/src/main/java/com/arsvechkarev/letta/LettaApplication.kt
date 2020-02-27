package com.arsvechkarev.letta

import android.app.Application
import android.content.res.Resources
import com.arsvechkarev.letta.graphics.FontManager
import timber.log.Timber

class LettaApplication : Application() {
  
  companion object {
    
    fun initResources(resources: Resources) {
      density = resources.displayMetrics.density
      scaledDensity = resources.displayMetrics.scaledDensity
    }
    
    var density: Float = -1f
      private set
    
    var scaledDensity: Float = -1f
      private set
  }
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    FontManager.loadFonts(applicationContext)
  }
  
}