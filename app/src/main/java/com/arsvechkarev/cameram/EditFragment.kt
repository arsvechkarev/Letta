package com.arsvechkarev.cameram


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit.buttonStickers
import kotlinx.android.synthetic.main.fragment_edit.drawingCanvas
import kotlinx.android.synthetic.main.fragment_edit.palette

class EditFragment : Fragment() {
  
  private val attachmentsFragment = AttachmentsFragment()
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_edit, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    palette.onColorSelected {
      drawingCanvas.setPaintColor(it)
    }
    
    buttonStickers.setOnClickListener {
      attachmentsFragment.show(childFragmentManager, "")
    }
  }
  
}
