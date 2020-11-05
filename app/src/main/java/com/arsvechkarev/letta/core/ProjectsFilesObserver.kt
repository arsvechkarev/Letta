package com.arsvechkarev.letta.core

object ProjectsFilesObserver {
  
  private var observer: Observer? = null
  
  fun setObserver(observer: Observer) {
    this.observer = observer
  }
  
  fun clearObserver() {
    this.observer = null
  }
  
  fun notifyProjectCreated(filename: String) {
    observer?.onNewProjectCreated(filename)
  }
  
  interface Observer {
    
    fun onNewProjectCreated(filename: String)
  }
}