package com.arsvechkarev.letta

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arsvechkarev.letta.editing.EditFragment
import com.arsvechkarev.letta.media.ImagesListFragment
import com.arsvechkarev.letta.utils.addKeyboardObserver
import timber.log.Timber

class MainActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LettaApplication.initResources(resources)
    setContentView(R.layout.activity_main)
    ActivityCompat.requestPermissions(
      this,
      arrayOf(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
      1
    )
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, ImagesListFragment())
        .commit()
  }

  override fun onBackPressed() {
    super.onBackPressed()
    Toast.makeText(this, "BackPressed", Toast.LENGTH_SHORT).show()
  }
}
