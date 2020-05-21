package com.arsvechkarev.letta

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arsvechkarev.letta.core.Navigator

class MainActivity : AppCompatActivity() {
  
  val navigator by lazy { NavigatorImpl(this@MainActivity) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LettaApplication.initDensities(resources.displayMetrics.density, resources.displayMetrics.scaledDensity)
    setContentView(R.layout.activity_main)
    ActivityCompat.requestPermissions(
      this,
      arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
      1
    )
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    navigator.openProjectsList()
  }
  
  override fun onBackPressed() {
    if (navigator.allowBackPressed()) {
      super.onBackPressed()
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    navigator.onDestroy()
  }
}
