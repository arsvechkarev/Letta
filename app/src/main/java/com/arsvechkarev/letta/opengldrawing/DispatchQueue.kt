package com.arsvechkarev.letta.opengldrawing

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.concurrent.CountDownLatch

open class DispatchQueue(threadName: String) : Thread() {
  
  private lateinit var handler: Handler
  private val syncLatch = CountDownLatch(1)
  
  init {
    name = threadName
    start()
  }
  
  fun sendMessage(msg: Message, delay: Int) {
    try {
      syncLatch.await()
      if (delay <= 0) {
        handler.sendMessage(msg)
      } else {
        handler.sendMessageDelayed(msg, delay.toLong())
      }
    } catch (ignore: Exception) {
    }
  }
  
  fun cancelRunnable(action: Action) {
    syncLatch.await()
    handler.removeCallbacks(action)
  }
  
  fun postRunnable(action: Action) {
    postRunnable(0, action)
  }
  
  fun postRunnable(delay: Long = 0, action: Action) {
    syncLatch.await()
    handler.postDelayed(action, delay)
  }
  
  override fun run() {
    Looper.prepare()
    handler = Handler()
    syncLatch.countDown()
    Looper.loop()
  }
}