package com.arsvechkarev.letta.core

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat.getFont
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.Font.MONTSERRAT
import com.arsvechkarev.letta.core.Font.RUBIK
import com.arsvechkarev.letta.core.Font.SCRIPT
import java.util.EnumMap

object FontManager {
  private var _fonts = EnumMap<Font, Typeface>(Font::class.java)
  val fonts: Map<Font, Typeface> = _fonts
  
  fun loadFonts(context: Context) {
    _fonts[MONTSERRAT] = getFont(context, R.font.montserrat)
    _fonts[RUBIK] = getFont(context, R.font.rubik)
    _fonts[SCRIPT] = getFont(context, R.font.script)
  }
  
}

enum class Font {
  MONTSERRAT,
  RUBIK,
  SCRIPT
}
