package com.arsvechkarev.letta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.letta.core.navigation.NavigatorImpl

class MainActivity : AppCompatActivity() {
  
  val navigator by lazy {
    NavigatorImpl(this@MainActivity)
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LettaApplication.initDensities(resources.displayMetrics.density, resources.displayMetrics.scaledDensity)
    setContentView(R.layout.activity_main)
    navigator.init(savedInstanceState)
  }
  
  override fun onBackPressed() {
    if (navigator.allowPressBack()) {
      super.onBackPressed()
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    navigator.onDestroy()
  }
}
