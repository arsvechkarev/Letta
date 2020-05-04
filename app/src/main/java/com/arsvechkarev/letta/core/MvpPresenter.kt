package com.arsvechkarev.letta.core

import androidx.lifecycle.ViewModel
import com.arsvechkarev.letta.core.async.Threader

abstract class MvpPresenter<V : MvpView>(
  private val threader: Threader = Threader
) : ViewModel() {
  
  protected var view: V? = null
  
  fun attachView(view: V) {
    this.view = view
  }
  
  override fun onCleared() {
    this.view = null
  }
  
  protected fun onIoThread(action: () -> Unit) {
    threader.onIoThread(action)
  }
  
  protected fun onBackground(action: () -> Unit) {
    threader.onBackground(action)
  }
  
  protected fun updateView(action: V.() -> Unit) {
    threader.onMainThread { view?.apply(action) }
  }
}