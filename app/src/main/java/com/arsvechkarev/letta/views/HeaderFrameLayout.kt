package com.arsvechkarev.letta.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.letta.views.behaviors.HeaderViewBehavior

/**
 * Marker for [com.arsvechkarev.letta.views.behaviors.ScrollingRecyclerBehavior]
 */
class HeaderFrameLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CoordinatorLayout.AttachedBehavior {
  
  override fun getBehavior(): CoordinatorLayout.Behavior<*> {
    return HeaderViewBehavior<HeaderFrameLayout>()
  }
}