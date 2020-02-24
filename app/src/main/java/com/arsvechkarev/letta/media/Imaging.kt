package com.arsvechkarev.letta.media

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.arsvechkarev.letta.core.Image

fun getImagesList(contentResolver: ContentResolver): ArrayList<Image> {
  val allImagesUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
  val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
  val order = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
  
  val cursor = contentResolver.query(allImagesUri, projection, null, null, order)
  val galleryImageUrls: ArrayList<Image> = ArrayList()
  cursor?.use {
    for (i in 0 until cursor.count) {
      cursor.moveToPosition(i)
      val dataColumnIndex: Int = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
      galleryImageUrls.add(Image(cursor.getString(dataColumnIndex)))
    }
  }
  return galleryImageUrls
}