package com.arsvechkarev.letta.extensions

import android.content.Context
import android.os.Environment
import java.io.File

val directoryProjects = "${File.separator}Letta${File.separator}Projects"

val Context.allProjectsDirectory: File
  get() {
    val picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(picturesDirectory, directoryProjects)
  }

fun Context.hasProjectFiles(): Boolean {
  val directory = allProjectsDirectory
  return directory.exists() && directory.isDirectory && directory.list()?.isNotEmpty() == true
}