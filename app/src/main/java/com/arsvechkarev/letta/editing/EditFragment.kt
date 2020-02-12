package com.arsvechkarev.letta.editing


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.KEY_IMAGE_URL
import com.arsvechkarev.letta.R

class EditFragment : Fragment(R.layout.fragment_edit) {
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  
  }
  
  companion object {
    
    fun create(imageUrl: String) = EditFragment().apply {
      arguments = bundleOf(KEY_IMAGE_URL to imageUrl)
    }
  }
  
}
