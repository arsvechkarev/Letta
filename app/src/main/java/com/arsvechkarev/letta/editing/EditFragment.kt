package com.arsvechkarev.letta.editing


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.constants.KEY_IMAGE_URL
import com.arsvechkarev.letta.utils.Group
import com.arsvechkarev.letta.utils.dmFloat
import com.arsvechkarev.letta.utils.dmInt
import com.arsvechkarev.letta.utils.inflate
import kotlinx.android.synthetic.main.fragment_edit.buttonCrop
import kotlinx.android.synthetic.main.fragment_edit.buttonPaint
import kotlinx.android.synthetic.main.fragment_edit.buttonStickers
import kotlinx.android.synthetic.main.fragment_edit.buttonText
import kotlinx.android.synthetic.main.fragment_edit.editRootLayout

class EditFragment : Fragment(R.layout.fragment_edit) {
  
  private val textFragment by lazy { TextFragment() }
  private val paintContainer by lazy {
    PaintContainer(buttonPaint, editRootLayout.inflate(R.layout.container_edit_paint))
  }
  private val stickersFragment by lazy { StickersFragment() }
  private val cropFragment by lazy { CropFragment() }
  
  private lateinit var toolsGroup: Group
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    toolsGroup = Group(buttonPaint, buttonText, buttonStickers, buttonCrop)
    buttonPaint.setOnClickListener {
      buttonPaint.animateToolMoveToTop(dmInt(R.dimen.img_tool_active),
        dmFloat(R.dimen.img_tool_m_top))
      toolsGroup.animateHide(except = buttonPaint)
      editRootLayout.addView(paintContainer.rootView)
    }
  }
  
  companion object {
    
    fun create(imageUrl: String) = EditFragment().apply {
      arguments = Bundle().apply {
        putString(KEY_IMAGE_URL, imageUrl)
      }
    }
  }
  
}
