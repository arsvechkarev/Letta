package com.arsvechkarev.letta.core.async

object Threader {
  
  private val backgroundWorker: Worker = BackgroundWorker.default()
  private val ioWorker: Worker = BackgroundWorker.io()
  private val mainThreadWorker: Worker = MainThreadWorker()
  
  fun onBackground(block: () -> Unit) {
    backgroundWorker.submit(block)
  }
  
  fun onIoThread(block: () -> Unit) {
    ioWorker.submit(block)
  }
  
  fun onMainThread(block: () -> Unit) {
    mainThreadWorker.submit(block)
  }
}