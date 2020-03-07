package com.arsvechkarev.letta.utils.extenstions

import kotlin.math.pow

fun Float.exponentiate(): Float {
  return this * 50 + (this * 6).pow(3.5f)
}