package com.arsvechkarev.letta.core.model

import android.graphics.Bitmap
import com.arsvechkarev.letta.core.recycler.DifferentiableItem

class Project(
  val filePath: String,
  val image: Bitmap
) : DifferentiableItem {
  
  override val id = filePath
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Project) return false
    if (filePath != other.filePath) return false
    return true
  }
  
  override fun hashCode(): Int {
    return filePath.hashCode()
  }
}