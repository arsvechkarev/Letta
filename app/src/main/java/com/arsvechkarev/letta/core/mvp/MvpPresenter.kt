package com.arsvechkarev.letta.core.mvp

import androidx.lifecycle.ViewModel
import com.arsvechkarev.letta.core.async.Threader

abstract class MvpPresenter<V : MvpView>(
  private val threader: Threader
) : ViewModel() {
  
  protected var view: V? = null
  
  open fun onViewAttached() {}
  
  open fun onViewCleared() {}
  
  fun attachView(view: V) {
    this.view = view
    onViewAttached()
  }
  
  override fun onCleared() {
    this.view = null
    onViewCleared()
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