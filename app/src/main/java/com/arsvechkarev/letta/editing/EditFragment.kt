package com.arsvechkarev.letta.editing


import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.CommonInjector
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.R.layout.container_edit_paint
import com.arsvechkarev.letta.R.layout.container_edit_text
import com.arsvechkarev.letta.animations.animateToolMoveBack
import com.arsvechkarev.letta.animations.animateToolMoveToTop
import com.arsvechkarev.letta.animations.fadeIn
import com.arsvechkarev.letta.animations.fadeOut
import com.arsvechkarev.letta.constants.KEY_IMAGE_URL
import com.arsvechkarev.letta.editing.EditFragment.Mode.CROP
import com.arsvechkarev.letta.editing.EditFragment.Mode.NONE
import com.arsvechkarev.letta.editing.EditFragment.Mode.PAINT
import com.arsvechkarev.letta.editing.EditFragment.Mode.STICKERS
import com.arsvechkarev.letta.editing.EditFragment.Mode.TEXT
import com.arsvechkarev.letta.graphics.TextVariant
import com.arsvechkarev.letta.utils.Group
import com.arsvechkarev.letta.utils.dmFloat
import com.arsvechkarev.letta.utils.inflateContainer
import kotlinx.android.synthetic.main.fragment_edit.bgImage
import kotlinx.android.synthetic.main.fragment_edit.buttonCrop
import kotlinx.android.synthetic.main.fragment_edit.buttonDone
import kotlinx.android.synthetic.main.fragment_edit.buttonPaint
import kotlinx.android.synthetic.main.fragment_edit.buttonStickers
import kotlinx.android.synthetic.main.fragment_edit.buttonText
import kotlinx.android.synthetic.main.fragment_edit.drawingCanvas
import kotlinx.android.synthetic.main.fragment_edit.editRoot
import kotlinx.android.synthetic.main.fragment_edit.viewPort

class EditFragment : Fragment(R.layout.fragment_edit) {
  
  private val imagesLoader = CommonInjector.getImagesLoader(this)
  
  private lateinit var textContainer: TextContainer
  private lateinit var paintContainer: PaintContainer
  
  private lateinit var toolsGroup: Group
  private var currentMode: Mode = NONE
  
  private val backCallback: OnBackPressedCallback = object : OnBackPressedCallback(false) {
    override fun handleOnBackPressed() {
      if (currentMode != NONE) {
        toggleMode(currentMode)
      }
    }
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (activity as ComponentActivity).onBackPressedDispatcher.addCallback(this, backCallback)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val url = arguments!!.getString(KEY_IMAGE_URL)!!
    imagesLoader.load(url).onSuccess {
      bgImage.setImageDrawable(it)
    }.start()
    toolsGroup = Group(buttonPaint, buttonText, buttonStickers, buttonCrop)
    textContainer =
        TextContainer(editRoot.inflateContainer(container_edit_text), buttonText, ::onTextEntered)
    paintContainer =
        PaintContainer(editRoot.inflateContainer(container_edit_paint), drawingCanvas, buttonPaint)
    buttonText.setOnClickListener { toggleMode(TEXT) }
    buttonPaint.setOnClickListener { toggleMode(PAINT) }
    buttonStickers.setOnClickListener { toggleMode(STICKERS) }
    buttonCrop.setOnClickListener { toggleMode(CROP) }
  }
  
  @Suppress("NON_EXHAUSTIVE_WHEN")
  private fun toggleMode(mode: Mode) {
    if (currentMode == NONE) {
      when (mode) {
        TEXT -> performModeSwitch(buttonText, textContainer)
        PAINT -> performModeSwitch(buttonPaint, paintContainer)
        STICKERS -> performModeSwitch(buttonStickers, paintContainer)
        CROP -> performModeSwitch(buttonCrop, paintContainer)
      }
      currentMode = mode
      backCallback.isEnabled = true
    } else {
      when (currentMode) {
        TEXT -> performModeExit(buttonText, textContainer)
        PAINT -> performModeExit(buttonPaint, paintContainer)
        STICKERS -> performModeExit(buttonStickers, paintContainer)
        CROP -> performModeExit(buttonCrop, paintContainer)
      }
      currentMode = NONE
      backCallback.isEnabled = false
    }
  }
  
  private fun performModeSwitch(topTool: View, container: Container) {
    buttonDone.fadeOut()
    topTool.animateToolMoveToTop(dmFloat(R.dimen.img_tool_m_top))
    toolsGroup.animateHide(except = topTool)
    editRoot.addView(container.view)
  }
  
  private fun performModeExit(topTool: View, container: Container) {
    buttonDone.fadeIn()
    topTool.animateToolMoveBack()
    toolsGroup.animateAppear(except = topTool)
    container.onExit(andThen = {
      editRoot.removeView(container.view)
    })
  }
  
  private fun onTextEntered(text: CharSequence, textVariant: TextVariant) {
    viewPort.addText(text, textVariant.paint)
    toggleMode(TEXT)
  }
  
  private enum class Mode {
    NONE,
    TEXT,
    PAINT,
    STICKERS,
    CROP
  }
  
  companion object {
    
    fun create(imageUrl: String) = EditFragment().apply {
      arguments = Bundle().apply {
        putString(KEY_IMAGE_URL, imageUrl)
      }
    }
  }
}
