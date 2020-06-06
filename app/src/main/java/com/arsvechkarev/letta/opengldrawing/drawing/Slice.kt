package com.arsvechkarev.letta.opengldrawing.drawing

import android.graphics.RectF
import com.arsvechkarev.letta.LettaApplication
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.zip.Deflater
import java.util.zip.Inflater

class Slice(
  data: ByteBuffer,
  private val bounds: RectF
) {
  
  private var file: File?
  
  val x: Int get() = bounds.left.toInt()
  val y: Int get() = bounds.top.toInt()
  val width: Int get() = bounds.width().toInt()
  val height: Int get() = bounds.height().toInt()
  
  init {
    val outputDir = LettaApplication.appContext.cacheDir
    file = File.createTempFile("paint", ".bin", outputDir)
    storeData(data)
  }
  
  val data: ByteBuffer
    get() {
      val input = ByteArray(1024)
      val output = ByteArray(1024)
      val fin = FileInputStream(file)
      val bos = ByteArrayOutputStream()
      val inflater = Inflater(true)
      while (true) {
        val numRead = fin.read(input)
        if (numRead != -1) {
          inflater.setInput(input, 0, numRead)
        }
        var numDecompressed: Int
        while (inflater.inflate(output, 0, output.size).also { numDecompressed = it } != 0) {
          bos.write(output, 0, numDecompressed)
        }
        if (inflater.finished()) {
          break
        } else if (inflater.needsInput()) {
          continue
        }
      }
      inflater.end()
      val result = ByteBuffer.wrap(bos.toByteArray(), 0, bos.size())
      bos.close()
      fin.close()
      return result
    }
  
  fun getBounds(): RectF {
    return RectF(bounds)
  }
  
  fun cleanResources() {
    file!!.delete()
    file = null
  }
  
  private fun storeData(data: ByteBuffer) {
    val input = data.array()
    val fos = FileOutputStream(file)
    val deflater = Deflater(Deflater.BEST_SPEED, true)
    deflater.setInput(input, data.arrayOffset(), data.remaining())
    deflater.finish()
    val buf = ByteArray(1024)
    while (!deflater.finished()) {
      val byteCount = deflater.deflate(buf)
      fos.write(buf, 0, byteCount)
    }
    deflater.end()
    fos.close()
  }
}