package com.arsvechkarev.opengldrawing

import android.os.Handler
import android.os.Looper

object Threader {
  
  private val handler = Handler(Looper.getMainLooper())
  
  fun onMainThread(block: () -> Unit) {
    handler.post(block)
  }
}