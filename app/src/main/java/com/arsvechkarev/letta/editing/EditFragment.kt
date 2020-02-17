package com.arsvechkarev.letta.editing


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.animations.animateToolMoveBack
import com.arsvechkarev.letta.animations.animateToolMoveToTop
import com.arsvechkarev.letta.constants.KEY_IMAGE_URL
import com.arsvechkarev.letta.editing.EditFragment.Mode.CROP
import com.arsvechkarev.letta.editing.EditFragment.Mode.NONE
import com.arsvechkarev.letta.editing.EditFragment.Mode.PAINT
import com.arsvechkarev.letta.editing.EditFragment.Mode.STICKERS
import com.arsvechkarev.letta.editing.EditFragment.Mode.TEXT
import com.arsvechkarev.letta.utils.Group
import com.arsvechkarev.letta.utils.dmFloat
import com.arsvechkarev.letta.utils.inflate
import kotlinx.android.synthetic.main.fragment_edit.buttonCrop
import kotlinx.android.synthetic.main.fragment_edit.buttonPaint
import kotlinx.android.synthetic.main.fragment_edit.buttonStickers
import kotlinx.android.synthetic.main.fragment_edit.buttonText
import kotlinx.android.synthetic.main.fragment_edit.editRootLayout

class EditFragment : Fragment(R.layout.fragment_edit) {
  
  private val textContainer by lazy { TextFragment() }
  private val paintContainer by lazy {
    PaintContainer(editRootLayout.inflate(R.layout.container_edit_paint))
  }
  private val stickersFragment by lazy { StickersFragment() }
  private val cropFragment by lazy { CropFragment() }
  
  private lateinit var toolsGroup: Group
  
  private var currentMode: Mode = NONE
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    toolsGroup = Group(buttonPaint, buttonText, buttonStickers, buttonCrop)
    buttonText.setOnClickListener { toggleMode(TEXT) }
    buttonPaint.setOnClickListener { toggleMode(PAINT) }
    buttonStickers.setOnClickListener { toggleMode(STICKERS) }
    buttonCrop.setOnClickListener { toggleMode(CROP) }
  }
  
  companion object {
    
    fun create(imageUrl: String) = EditFragment().apply {
      arguments = Bundle().apply {
        putString(KEY_IMAGE_URL, imageUrl)
      }
    }
  }
  
  @Suppress("NON_EXHAUSTIVE_WHEN")
  private fun toggleMode(mode: Mode) {
    if (currentMode == NONE) {
      when (mode) {
        TEXT -> performModeSwitch(buttonText, paintContainer)
        PAINT -> performModeSwitch(buttonPaint, paintContainer)
        STICKERS -> performModeSwitch(buttonStickers, paintContainer)
        CROP -> performModeSwitch(buttonCrop, paintContainer)
      }
      currentMode = mode
    } else {
      when (currentMode) {
        TEXT -> performModeReturn(buttonText, paintContainer)
        PAINT -> performModeReturn(buttonPaint, paintContainer)
        STICKERS -> performModeReturn(buttonStickers, paintContainer)
        CROP -> performModeReturn(buttonCrop, paintContainer)
      }
      currentMode = NONE
    }
  }
  
  private fun performModeReturn(topTool: View, container: Container) {
    topTool.animateToolMoveBack()
    toolsGroup.animateAppear(except = topTool)
    container.animateExit(andThen = {
      editRootLayout.removeView(container.view)
    })
  }
  
  private fun performModeSwitch(topTool: View, container: Container) {
    topTool.animateToolMoveToTop(dmFloat(R.dimen.img_tool_m_top))
    toolsGroup.animateHide(except = topTool)
    editRootLayout.addView(container.view)
    container.animateEnter()
  }
  
  private enum class Mode {
    NONE,
    TEXT,
    PAINT,
    STICKERS,
    CROP
  }
}
