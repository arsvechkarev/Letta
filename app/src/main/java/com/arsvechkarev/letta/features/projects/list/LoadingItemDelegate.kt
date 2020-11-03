package com.arsvechkarev.letta.features.projects.list

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.LoadingMoreProjects
import com.arsvechkarev.letta.core.recycler.DelegateViewHolder
import com.arsvechkarev.letta.core.recycler.ListAdapterDelegate
import com.arsvechkarev.letta.extensions.getDimen
import com.arsvechkarev.letta.extensions.i
import com.arsvechkarev.letta.views.ProgressBar

class LoadingItemDelegate : ListAdapterDelegate<LoadingMoreProjects>(
  LoadingMoreProjects::class
) {
  
  override fun onCreateViewHolder(
    parent: ViewGroup
  ): DelegateViewHolder<LoadingMoreProjects> {
    val frameLayout = FrameLayout(parent.context).apply {
      layoutParams = ViewGroup.MarginLayoutParams(parent.measuredWidth, WRAP_CONTENT).apply {
        val margin = parent.context.getDimen(R.dimen.project_progress_bar_margin).i
        bottomMargin = margin
      }
      val size = parent.context.getDimen(R.dimen.project_progress_bar_small_size).i
      addView(ProgressBar(parent.context), FrameLayout.LayoutParams(size, size).apply {
        gravity = Gravity.CENTER
      })
    }
    return object : DelegateViewHolder<LoadingMoreProjects>(frameLayout) {}
  }
}