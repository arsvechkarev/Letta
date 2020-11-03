package com.arsvechkarev.letta.core.model

import com.arsvechkarev.letta.core.recycler.DifferentiableItem

object LoadingMoreProjects : DifferentiableItem {
  
  override val id = "LoadingMoreProjects"
  
  override fun equals(other: Any?): Boolean {
    return other === LoadingMoreProjects
  }
}