package com.arsvechkarev.letta.core.model

import android.graphics.Bitmap
import com.arsvechkarev.letta.core.recycler.DifferentiableItem

class Project(
  _id: Long,
  val filename: String,
  val image: Bitmap
) : DifferentiableItem {
  
  val idLong = _id
  override val id = _id.toString()
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Project) return false
    if (id != other.id) return false
    return true
  }
  
  override fun hashCode(): Int {
    var result = filename.hashCode()
    result = 31 * result + id.hashCode()
    return result
  }
}