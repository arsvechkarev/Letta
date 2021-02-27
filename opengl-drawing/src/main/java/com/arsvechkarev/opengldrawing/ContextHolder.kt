package com.arsvechkarev.opengldrawing

import android.content.Context

object ContextHolder {
  
  private var _applicationContext: Context? = null
  val applicationContext: Context get() = _applicationContext!!
  
  fun setApplicationContext(applicationContext: Context) {
    _applicationContext = applicationContext
  }
}