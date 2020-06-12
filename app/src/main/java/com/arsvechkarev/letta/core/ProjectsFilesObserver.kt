package com.arsvechkarev.letta.core

object ProjectsFilesObserver {
  
  private var observer: Observer? = null
  
  fun setObserver(observer: Observer) {
    this.observer = observer
  }
  
  fun clearObserver() {
    this.observer = null
  }
  
  fun notifyProjectCreated(projectFullPath: String) {
    observer?.onNewProjectCreated(projectFullPath)
  }
  
  interface Observer {
    
    fun onNewProjectCreated(projectFullPath: String)
  }
}