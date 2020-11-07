package com.arsvechkarev.letta.core.model

import com.arsvechkarev.letta.core.recycler.DifferentiableItem

object LoadingMoreProjects : DifferentiableItem {
  
  override val id = Long.MAX_VALUE
  
  override fun equals(other: Any?): Boolean {
    return other === LoadingMoreProjects
  }
}