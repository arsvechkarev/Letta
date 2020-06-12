package com.arsvechkarev.letta.core.animations

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator

val LinearInterpolator = LinearInterpolator()
val AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
val EndOvershootInterpolator = OvershootInterpolator()
val StartOvershootInterpolator = OvershootInterpolator(-4f)