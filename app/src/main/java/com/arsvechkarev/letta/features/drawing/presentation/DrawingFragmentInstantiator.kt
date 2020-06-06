package com.arsvechkarev.letta.features.drawing.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import androidx.annotation.DrawableRes
import com.arsvechkarev.letta.core.model.Project

object DrawingFragmentInstantiator {
  
  private const val KEY = "KEY"
  private const val TYPE = "TYPE"
  
  private const val FILE_PATH = "FILE_PATH"
  private const val COLOR = "COLOR"
  private const val DRAWABLE_RES = "DRAWABLE_RES"
  
  fun ofProject(project: Project) = DrawingFragment().apply {
    arguments = Bundle().apply {
      putString(TYPE, FILE_PATH)
      putString(KEY, project.filePath)
    }
  }
  
  fun ofColor(color: Int) = DrawingFragment().apply {
    arguments = Bundle().apply {
      putString(TYPE, COLOR)
      putInt(KEY, color)
    }
  }
  
  fun ofDrawableRes(@DrawableRes drawableRes: Int) = DrawingFragment().apply {
    arguments = Bundle().apply {
      putString(TYPE, DRAWABLE_RES)
      putInt(KEY, drawableRes)
    }
  }
  
  fun getBitmapBy(context: Context, arguments: Bundle, width: Int, height: Int): Bitmap {
    when (arguments.get(TYPE)) {
      FILE_PATH -> {
        val bitmapFilename = arguments.getString(KEY)!!
        return BitmapFactory.decodeFile(bitmapFilename)
      }
      COLOR -> {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val color = arguments.getInt(COLOR)
        canvas.drawColor(color)
        return bitmap
      }
      DRAWABLE_RES -> {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val drawableRes = arguments.getInt(DRAWABLE_RES)
        val drawable = context.getDrawable(drawableRes)
        drawable!!.draw(canvas)
        return bitmap
      }
      else -> throw IllegalStateException("Unknown type")
    }
  }
}