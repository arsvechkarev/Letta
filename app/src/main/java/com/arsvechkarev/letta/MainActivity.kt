package com.arsvechkarev.letta

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arsvechkarev.letta.features.drawing.presentation.DrawingFragment

class MainActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LettaApplication.initResources(resources)
    setContentView(R.layout.activity_main)
    ActivityCompat.requestPermissions(
      this,
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE),
      1
    )
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, DrawingFragment())
        .commit()
  }
}
