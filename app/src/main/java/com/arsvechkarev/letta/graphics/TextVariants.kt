package com.arsvechkarev.letta.graphics

import android.graphics.Color
import android.graphics.Color.GREEN
import android.graphics.Color.WHITE
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.Typeface.DEFAULT
import android.graphics.Typeface.DEFAULT_BOLD
import android.graphics.Typeface.ITALIC
import android.graphics.Typeface.create
import android.text.TextPaint
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.graphics.Font.SCRIPT
import com.arsvechkarev.letta.graphics.FontManager.fonts

class TextVariant(
  val text: CharSequence,
  val textSizeResId: Int,
  val paint: TextPaint
)

class TextVariantBuilder {
  var text: CharSequence? = null
  var textSizeResId: Int = R.dimen.text_variant_default
  private var paint: TextPaint? = null
  
  fun build(): TextVariant {
    return TextVariant(text!!, textSizeResId,
      paint!!)
  }
  
  fun paint(block: TextPaint.() -> Unit) {
    paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply(block).apply {
      style = Paint.Style.FILL
    }
  }
}

fun textVariant(block: TextVariantBuilder.() -> Unit): TextVariant {
  return TextVariantBuilder().apply(block).build()
}

val textVariantsList by lazy {
  listOf(
  
    textVariant {
      text = "Simple"
      paint {
        color = WHITE
      }
    },
  
    textVariant {
      text = "Bold"
      paint {
        typeface = DEFAULT_BOLD
        color = WHITE
      }
    },
  
    textVariant {
      text = "Script"
      paint {
        typeface = fonts[SCRIPT]
        color = GREEN
      }
    },
  
    textVariant {
      text = "Italic"
      paint {
        typeface = create(DEFAULT, ITALIC)
        color = Color.MAGENTA
      }
    },
  
    textVariant {
      text = "Rainbow"
      paint {
        typeface = create(DEFAULT, ITALIC)
        color = Color.RED
      }
    },
    
    textVariant {
      text = "Glow"
      paint {
        typeface = create(DEFAULT, ITALIC)
        color = Color.CYAN
      }
    }
  )
}

private fun paint(block: Paint.() -> Unit): TextPaint {
  return TextPaint(Paint.ANTI_ALIAS_FLAG).apply(block)
}