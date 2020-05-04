package com.arsvechkarev.letta.utils

import android.content.Context
import android.os.Environment
import java.io.File

fun Context.hasFilesWithin(directoryPath: String): Boolean {
  val baseDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  val directory = File(baseDir, directoryPath)
  return directory.exists() && directory.isDirectory && directory.list()?.isNotEmpty() == true
}