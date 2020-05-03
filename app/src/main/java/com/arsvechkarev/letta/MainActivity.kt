package com.arsvechkarev.letta

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.letta.features.drawing.DrawingFragment

class MainActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LettaApplication.initResources(resources)
    setContentView(R.layout.activity_main)
    savedInstanceState ?: supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, DrawingFragment())
        .commit()
  }
}
