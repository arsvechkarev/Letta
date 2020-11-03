package com.arsvechkarev.letta.core.mvp

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.letta.core.navigation.NavigableFragment
import kotlin.reflect.KClass

abstract class MvpFragment<V : MvpView, P : MvpPresenter<V>>(
  private val presenterClass: KClass<P>,
  @LayoutRes layout: Int = 0
) : Fragment(layout), MvpView, NavigableFragment {
  
  private lateinit var _presenter: P
  
  val presenter: P get() = _presenter
  
  abstract fun createPresenter(): P
  
  @Suppress("UNCHECKED_CAST")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _presenter = ViewModelProvider(this, object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return createPresenter() as T
      }
    }).get(presenterClass.java)
    _presenter.attachView(this as V)
  }
}