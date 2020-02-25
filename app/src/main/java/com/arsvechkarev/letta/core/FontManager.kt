package com.arsvechkarev.letta.core

import android.content.Context
import android.graphics.Typeface
import com.arsvechkarev.letta.core.Font.MONTSERRAT
import com.arsvechkarev.letta.core.Font.RUBIK
import com.arsvechkarev.letta.core.Font.SCRIPT
import java.util.EnumMap

object FontManager {
  
  private var _fonts = EnumMap<Font, Typeface>(Font::class.java)
  val fonts: Map<Font, Typeface> = _fonts
  
  fun loadFonts(context: Context) {
    _fonts[MONTSERRAT] = Typeface.DEFAULT_BOLD/*getFont(context, R.font.montserrat)*/
    _fonts[RUBIK] = Typeface.DEFAULT_BOLD/*getFont(context, R.font.rubik)*/
    _fonts[SCRIPT] = Typeface.DEFAULT_BOLD/*getFont(context, R.font.script)*/
  }
  
}

enum class Font {
  MONTSERRAT,
  RUBIK,
  SCRIPT
}
