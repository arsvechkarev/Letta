package com.arsvechkarev.letta.core

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.arsvechkarev.letta.utils.inflate
import com.arsvechkarev.letta.views.ContainerView

abstract class Container(private val rootView: ViewGroup, @LayoutRes layoutResId: Int) :
  ContainerView.EventListener {
  
  val containerView = rootView.inflate(layoutResId) as ContainerView
  
  init {
    containerView.listener = this
  }
  
  fun <T : View> findViewById(@IdRes id: Int): T {
    return rootView.findViewById(id) ?: throw IllegalStateException(
      "View with id $id not found")
  }
}