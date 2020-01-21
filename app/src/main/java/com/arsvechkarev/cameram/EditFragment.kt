package com.arsvechkarev.cameram


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_edit.drawingCanvas
import kotlinx.android.synthetic.main.fragment_edit.palette

class EditFragment : Fragment() {
  
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
  }
  
}
