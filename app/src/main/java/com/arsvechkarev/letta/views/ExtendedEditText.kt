package com.arsvechkarev.letta.views

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText

class ExtendedEditText(
  context: Context,
  attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
  
  var onBackPressed: (String) -> Unit = {}
  
  override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      onBackPressed(text?.toString() ?: "")
      val methodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      methodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
    return false
  }
  
}