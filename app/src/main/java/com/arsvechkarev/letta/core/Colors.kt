package com.arsvechkarev.letta.core

import android.content.Context
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.extensions.getAttrColor

object Colors {
  
  const val Transparent = 0
  const val Disabled = 0xFF999999.toInt()
  const val GraySelected = 0xFFAAAAAA.toInt()
  const val BorderVeryDark = 0xFF444444.toInt()
  const val BorderDark = 0xFF555555.toInt()
  const val BorderLight = 0xFFAAAAAA.toInt()
  const val BorderVeryLight = 0xFFDDDDDD.toInt()
  
  private val colors = IntArray(17) { 0 }
  
  val Background get() = colors[0]
  val BottomSheet get() = colors[1]
  val IconDefault get() = colors[2]
  val IconOpposite get() = colors[3]
  val Button get() = colors[4]
  val ButtonGradientStart get() = colors[5]
  val ButtonGradientEnd get() = colors[6]
  val TextPrimary get() = colors[7]
  val TextOnButton get() = colors[8]
  val Checkmark get() = colors[9]
  val Divider get() = colors[10]
  val Shadow get() = colors[11]
  val Failure get() = colors[12]
  val Ripple get() = colors[13]
  val RippleFailure get() = colors[14]
  val RippleDark get() = colors[15]
  val StatusBar get() = colors[16]
  
  fun init(context: Context) {
    colors[0] = context.getAttrColor(R.attr.colorBackground)
    colors[1] = context.getAttrColor(R.attr.colorBottomSheet)
    colors[2] = context.getAttrColor(R.attr.colorIconDefault)
    colors[3] = context.getAttrColor(R.attr.colorIconOpposite)
    colors[4] = context.getAttrColor(R.attr.colorButton)
    colors[5] = context.getAttrColor(R.attr.colorButtonGradientStart)
    colors[6] = context.getAttrColor(R.attr.colorButtonGradientEnd)
    colors[7] = context.getAttrColor(R.attr.colorTextPrimary)
    colors[8] = context.getAttrColor(R.attr.colorTextOnButton)
    colors[9] = context.getAttrColor(R.attr.colorCheckmark)
    colors[10] = context.getAttrColor(R.attr.colorDivider)
    colors[11] = context.getAttrColor(R.attr.colorShadow)
    colors[12] = context.getAttrColor(R.attr.colorFailure)
    colors[13] = context.getAttrColor(R.attr.colorRipple)
    colors[14] = context.getAttrColor(R.attr.colorRippleFailure)
    colors[15] = context.getAttrColor(R.attr.colorRippleDark)
    colors[16] = context.getAttrColor(R.attr.colorStatusBar)
  }
}