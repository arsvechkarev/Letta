package com.arsvechkarev.letta.features.drawing.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.extensions.getProjectPath

private const val TYPE = "TYPE"
private const val KEY_WIDTH = "KEY_WIDTH"
private const val KEY_HEIGHT = "KEY_HEIGHT"

private const val PROJECT_FILENAME = "PROJECT_FILENAME"
private const val COLOR = "COLOR"
private const val DRAWABLE_RES = "DRAWABLE_RES"

fun createProjectArgs(project: Project, width: Int, height: Int) = Bundle().apply {
  putString(TYPE, PROJECT_FILENAME)
  putString(PROJECT_FILENAME, project.filename)
  putInt(KEY_WIDTH, width)
  putInt(KEY_HEIGHT, height)
}

fun createColorArgs(color: Int, width: Int, height: Int) = Bundle().apply {
  putString(TYPE, COLOR)
  putInt(COLOR, color)
  putInt(KEY_WIDTH, width)
  putInt(KEY_HEIGHT, height)
}

fun createDrawableResArgs(@DrawableRes drawableRes: Int, width: Int, height: Int) = Bundle().apply {
  putString(TYPE, DRAWABLE_RES)
  putInt(DRAWABLE_RES, drawableRes)
  putInt(KEY_WIDTH, width)
  putInt(KEY_HEIGHT, height)
}

fun getBitmapBy(context: Context, arguments: Bundle): Bitmap {
  when (arguments.get(TYPE)) {
    PROJECT_FILENAME -> {
      val bitmapFilename = arguments.getString(PROJECT_FILENAME)!!
      return BitmapFactory.decodeFile(context.getProjectPath(bitmapFilename))
    }
    COLOR -> {
      val width = arguments.getInt(KEY_WIDTH)
      val height = arguments.getInt(KEY_HEIGHT)
      val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bitmap)
      val color = arguments.getInt(COLOR)
      canvas.drawColor(color)
      return bitmap
    }
    DRAWABLE_RES -> {
      val width = arguments.getInt(KEY_WIDTH)
      val height = arguments.getInt(KEY_HEIGHT)
      val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bitmap)
      val drawableRes = arguments.getInt(DRAWABLE_RES)
      val drawable = ContextCompat.getDrawable(context, drawableRes)!!
      drawable.setBounds(0, 0, width, height)
      drawable.draw(canvas)
      return bitmap
    }
    else -> throw IllegalStateException("Unknown type")
  }
}