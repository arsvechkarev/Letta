package com.arsvechkarev.letta

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.letta.core.Colors
import com.arsvechkarev.letta.core.navigation.NavigatorImpl

class MainActivity : AppCompatActivity() {
  
  val navigator by lazy { NavigatorImpl(this@MainActivity) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    @Suppress("DEPRECATION")
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    LettaApplication.initDensities(resources.displayMetrics.density,
      resources.displayMetrics.scaledDensity)
    Colors.init(this)
    setContentView(R.layout.activity_main)
    navigator.init(savedInstanceState)
  }
  
  override fun onBackPressed() {
    if (navigator.allowBackPress()) {
      super.onBackPressed()
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    navigator.onDestroy()
  }
}