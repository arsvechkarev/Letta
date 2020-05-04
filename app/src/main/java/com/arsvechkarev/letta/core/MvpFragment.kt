package com.arsvechkarev.letta.core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KClass

abstract class MvpFragment<V : MvpView, P : MvpPresenter<V>>(
  private val presenterClass: KClass<P>,
  @LayoutRes layout: Int = 0
) : Fragment(layout), MvpView {
  
  protected lateinit var presenter: P
    private set
  
  abstract fun createPresenter(): P
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    presenter = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return createPresenter() as T
      }
    }).get(presenterClass.java)
    presenter.attachView(this as V)
  }
}