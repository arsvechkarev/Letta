package com.arsvechkarev.cameram


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit.buttonUndo
import kotlinx.android.synthetic.main.fragment_edit.drawingCanvas
import kotlinx.android.synthetic.main.fragment_edit.palette
import kotlinx.android.synthetic.main.fragment_edit.seekbar

class EditFragment : Fragment() {
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_edit, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    palette.onColorSelected { drawingCanvas.setPaintColor(it) }
    seekbar.onPercentChanged { drawingCanvas.changeWidth(it * 30) }
    buttonUndo.setOnClickListener { drawingCanvas.undo() }
  }
  
}
