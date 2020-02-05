package com.arsvechkarev.letta.media.common

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.letta.core.Image
import com.arsvechkarev.letta.media.getImagesList

class ImagesViewModel(private val contentResolver: ContentResolver) : ViewModel() {
  
  var data: List<Image> = ArrayList()
  var currentPosition = 0
  
  fun loadDataIfNecessary(): List<Image> {
    if (data.isNotEmpty()) return data
    data = getImagesList(contentResolver)
    return data.also { list ->
      list.forEach{
        println("img = ${it.url}")
      }
    }
  }
  
  @Suppress("UNCHECKED_CAST")
  class Factory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass != ImagesViewModel::class.java) {
        throw IllegalArgumentException("Unknown view model class: $modelClass")
      }
      return ImagesViewModel(contentResolver) as T
    }
    
  }
}