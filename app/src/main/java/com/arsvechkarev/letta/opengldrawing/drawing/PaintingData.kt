package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.Bitmap
import java.nio.ByteBuffer

class PaintingData(
  @JvmField var bitmap: Bitmap?,
  @JvmField var byteBuffer: ByteBuffer?
)