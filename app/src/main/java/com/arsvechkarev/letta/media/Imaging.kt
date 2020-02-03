package com.arsvechkarev.letta.media

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.arsvechkarev.letta.core.Image
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

interface ImagesLoader {
  
  fun loadImage(url: String, imageView: ImageView, @DrawableRes placeholderResId: Int)
}

class GlideImagesLoader(private val fragment: Fragment) : ImagesLoader {
  
  override fun loadImage(url: String, imageView: ImageView, placeholderResId: Int) {
    Glide.with(fragment)
      .load(url)
      .centerCrop()
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .placeholder(placeholderResId)
      .into(imageView)
  }
  
}

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